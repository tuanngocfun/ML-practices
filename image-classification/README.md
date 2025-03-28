## CNN Model with CINIC-10 Lance image dataset

### Overview
This example demonstrates how to train a Convolutional Neural Network (CNN) model using the CINIC-10 image dataset in the Lance format. The [CINIC-10](https://paperswithcode.com/dataset/cinic-10) dataset consists of 10 classes of images, including airplanes, automobiles, birds, cats, deer, dogs, frogs, horses, ships, and trucks.

### Dataset
The script assumes that you have the CINIC-10 dataset in the [Lance](https://www.kaggle.com/datasets/vipulmaheshwarii/cinic-10-lance-dataset) format, with the following directory structure:

```python
cinic/
    cinic_train.lance
    cinic_test.lance
    cinic_val.lance
```

If you don't have the dataset in the Lance format, you can use the `convert-any-image-dataset-to-lance.py` script provided in the `converters` folder to convert your dataset to the Lance format.

To get started, install the necessary Python packages by running the following command:

```python
pip install torch torchvision tqdm pillow pylance wandb
```

### Usage
1. Clone or download the repository.
2. Navigate to the directory containing the `cnn-model-with-lance-dataset.ipynb` script.
3. Open the Jupyter Notebook or run the script using an appropriate environment.
4. The script will load the dataset, preprocess the images, define the CNN model architecture, and train the model using the provided hyperparameters.
5. During training, the script will log the loss, validation accuracy, and epoch duration to the wandb dashboard.
6. After training, the script will save the trained model as `cinic_resnet.pth`.
7. Finally, the script will evaluate the trained model on the test dataset and log the test accuracy to the wandb dashboard.

### Customization

You can customize the script by modifying the following parameters:

1. `lr`: Learning rate for the optimizer
2. `momentum`: Momentum value for the optimizer
3. `number_of_epochs`: Number of epochs for training
4. `model_batch_size`: Batch size for training and evaluation
5. `batches_to_train`: Number of batches you want to Train

### Results

The script will log the training loss, validation accuracy, epoch duration, and test accuracy to the wandb dashboard. You can monitor the training progress and evaluate the model's performance using the wandb dashboard.

### Additional Resources

- [PyTorch Documentation](https://pytorch.org/docs/stable/index.html)
- [Lance File Format](https://github.com/lancedb/lance)
- [wandb Documentation](https://docs.wandb.ai/)
- [CINIC-10 Dataset](https://paperswithcode.com/dataset/cinic-10)
- [CINIC-10 Lance Dataset](https://www.kaggle.com/datasets/vipulmaheshwarii/cinic-10-lance-dataset)


### Code
<a href="https://colab.research.google.com/drive/1q2fXXH81yTEcHwqAJPX8DR6GVBwuZwSg?usp=sharing"><img src="https://colab.research.google.com/assets/colab-badge.svg" alt="Open In Colab"></a>