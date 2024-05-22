package org.paceWithMe.hadoop.helpers;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.jobcontrol.ControlledJob;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MerchantAnalyticsJob extends Configured implements Tool {

    private final static Logger LOGGER = LoggerFactory.getLogger(MerchantAnalyticsJob.class);
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static class MerchantPartitioner extends Partitioner<Text, AggregateWritable> {
        @Override
        public int getPartition(Text key, AggregateWritable value, int numPartitions) {
            return Math.abs(key.toString().hashCode()) % numPartitions;
        }
    }

    public static class TransactionMapper extends Mapper<LongWritable, Text, Text, AggregateWritable> {

        private static Map<String, String> merchantIdNameMap = new HashMap<>();

        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            URI[] paths = context.getCacheArchives();
            if (paths != null) {
                for (URI path : paths) {
                    loadMerchantIdNameInCache(path.toString(), context.getConfiguration());
                }
            }
            super.setup(context);
        }

        private void loadMerchantIdNameInCache(String file, org.apache.hadoop.conf.Configuration conf) {
            LOGGER.info("file name : " + file);
            String strRead;
            try (BufferedReader br = new BufferedReader(new InputStreamReader(FileSystem.get(conf).open(new Path(file))))) {
                while ((strRead = br.readLine()) != null) {
                    String line = strRead.replace("\"", "");
                    String[] splitarray = line.split(",");
                    merchantIdNameMap.put(splitarray[0], splitarray[2]);
                }
            } catch (Exception e) {
                LOGGER.error("exception occurred while loading data in cache", e);
            }
        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString().replace("\"", "");

            if (line.contains("transaction")) {
                return;
            }

            String[] split = line.split(",");
            Transaction transaction = new Transaction();
            transaction.setTxId(split[0]);
            transaction.setCustomerId(Long.parseLong(split[1]));
            transaction.setMerchantId(Long.parseLong(split[2]));
            transaction.setTimeStamp(split[3].trim().split(" ")[0].trim());
            transaction.setInvoiceNumber(split[4].trim());
            transaction.setInvoiceAmount(Float.parseFloat(split[5]));
            transaction.setSegment(split[6].trim());

            AggregateData aggregatedata = new AggregateData();
            if (transaction.getInvoiceAmount() <= 5000)
                aggregatedata.setOrderBelow5000(1L);
            else if (transaction.getInvoiceAmount() <= 10000)
                aggregatedata.setOrderBelow10000(1L);
            else if (transaction.getInvoiceAmount() <= 20000)
                aggregatedata.setOrderBelow20000(1L);
            else
                aggregatedata.setOrderAbove20000(1L);

            aggregatedata.setTotalOrder(1L);
            String outputkey = merchantIdNameMap.get(transaction.getMerchantId().toString()) + "-" + split[3].trim().split(" ")[0].trim();
            context.write(new Text(outputkey), new AggregateWritable(aggregatedata));
        }
    }

    public static class MerchantOrderReducer extends Reducer<Text, AggregateWritable, Text, AggregateWritable> {

        public void reduce(Text key, Iterable<AggregateWritable> values, Context context) throws IOException, InterruptedException {
            AggregateData aggregatedata = new AggregateData();

            for (AggregateWritable val : values) {
                aggregatedata.setOrderBelow20000(aggregatedata.getOrderBelow20000() + val.getAggregateData().getOrderBelow20000());
                aggregatedata.setOrderAbove20000(aggregatedata.getOrderAbove20000() + val.getAggregateData().getOrderAbove20000());
                aggregatedata.setOrderBelow10000(aggregatedata.getOrderBelow10000() + val.getAggregateData().getOrderBelow10000());
                aggregatedata.setOrderBelow5000(aggregatedata.getOrderBelow5000() + val.getAggregateData().getOrderBelow5000());
                aggregatedata.setTotalOrder(aggregatedata.getTotalOrder() + val.getAggregateData().getTotalOrder());
            }

            context.write(key, new AggregateWritable(aggregatedata));
        }
    }

    public static void main(String[] args) {
        try {
            ToolRunner.run(new Configuration(), new MerchantAnalyticsJob(), args);
        } catch (Exception e) {
            LOGGER.error("Exception occurred during job execution", e);
        }
        System.exit(0);
    }

    public static int runMRJob(String[] args) throws Exception {
        Configuration conf = new Configuration();
        ControlledJob myJob1 = new ControlledJob(conf);
        Job job = myJob1.getJob();

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(AggregateWritable.class);
        job.setJarByClass(MerchantAnalyticsJob.class);

        job.setReducerClass(MerchantOrderReducer.class);
        FileInputFormat.setInputDirRecursive(job, true);
        MultipleInputs.addInputPath(job, new Path(args[0]), TextInputFormat.class, TransactionMapper.class);
        FileSystem filesystem = FileSystem.get(job.getConfiguration());
        RemoteIterator<LocatedFileStatus> files = filesystem.listFiles(new Path(args[1]), true);
        while (files.hasNext()) {
            job.addCacheArchive(files.next().getPath().toUri());
        }

        FileOutputFormat.setOutputPath(job, new Path(args[2] + "/" + Calendar.getInstance().getTimeInMillis()));
        job.setNumReduceTasks(5);
        job.setPartitionerClass(MerchantPartitioner.class);
        return job.waitForCompletion(true) ? 0 : 1;
    }

    @Override
    public int run(String[] args) throws Exception {
        return runMRJob(args);
    }
}
