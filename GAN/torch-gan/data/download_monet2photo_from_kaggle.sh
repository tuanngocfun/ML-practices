#!/bin/bash

# Name of the dataset
DATASET="monet2photo"

# Kaggle dataset URL
KAGGLE_DATASET="balraj98/monet2photo"

# Directory paths
ZIP_FILE="./$DATASET.zip"
TARGET_DIR="./$DATASET"

# Check if Kaggle CLI is installed
if ! command -v kaggle &> /dev/null
then
    echo "Kaggle CLI is not installed. Please install it first (https://github.com/Kaggle/kaggle-api)."
    exit 1
fi

# Download dataset
echo "Downloading $DATASET dataset from Kaggle..."
kaggle datasets download -d $KAGGLE_DATASET -p . --force

# Check if the zip file exists
if [ ! -f "$ZIP_FILE" ]; then
    echo "Error: $ZIP_FILE not found after download."
    exit 1
fi

# Extract dataset
echo "Extracting $ZIP_FILE..."
unzip -q $ZIP_FILE -d $TARGET_DIR
rm $ZIP_FILE

# Organize dataset structure
echo "Organizing dataset structure..."
mkdir -p "$TARGET_DIR/train/A" "$TARGET_DIR/train/B" "$TARGET_DIR/test/A" "$TARGET_DIR/test/B"
mv "$TARGET_DIR/trainA/"* "$TARGET_DIR/train/A/"  // Monet paintings
mv "$TARGET_DIR/trainB/"* "$TARGET_DIR/train/B/" //Natural photos
mv "$TARGET_DIR/testA/"* "$TARGET_DIR/test/A/"   // Monet paintings
mv "$TARGET_DIR/testB/"* "$TARGET_DIR/test/B/"  //Natural photos
rm -rf "$TARGET_DIR/trainA" "$TARGET_DIR/trainB" "$TARGET_DIR/testA" "$TARGET_DIR/testB"

echo "Dataset $DATASET is ready at $TARGET_DIR!"
