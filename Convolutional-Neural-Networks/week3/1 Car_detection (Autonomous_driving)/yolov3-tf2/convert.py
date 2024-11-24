from absl import app, flags, logging
from absl.flags import FLAGS
import numpy as np
from yolov3_tf2.models import YoloV3, YoloV3Tiny
from yolov3_tf2.utils import load_darknet_weights
import tensorflow as tf

flags.DEFINE_string('weights', './data/yolov3.weights', 'path to weights file')
flags.DEFINE_string('output', './saved_model', 'path to output')
flags.DEFINE_boolean('tiny', False, 'yolov3 or yolov3-tiny')
flags.DEFINE_integer('num_classes', 80, 'number of classes in the model')

def main(_argv):
    physical_devices = tf.config.experimental.list_physical_devices('GPU')
    if len(physical_devices) > 0:
        tf.config.experimental.set_memory_growth(physical_devices[0], True)

    # Load the YOLO model
    if FLAGS.tiny:
        yolo = YoloV3Tiny(classes=FLAGS.num_classes)
    else:
        yolo = YoloV3(classes=FLAGS.num_classes)
    yolo.summary()
    logging.info('Model created')

    # Load weights from Darknet format
    load_darknet_weights(yolo, FLAGS.weights, FLAGS.tiny)
    logging.info('Weights loaded')

    # Run a random image through the model to initialize it
    img = np.random.random((1, 320, 320, 3)).astype(np.float32)
    _ = yolo(img)
    logging.info('Sanity check passed')

    # Save the model in SavedModel format
    tf.saved_model.save(yolo, FLAGS.output)
    logging.info(f'Model saved in SavedModel format at {FLAGS.output}')

if __name__ == '__main__':
    try:
        app.run(main)
    except SystemExit:
        pass
