#!/bin/bash

# Update and install packages
echo "Updating and installing necessary packages..."
sudo apt update
sudo apt install -y python3-pip python3-venv

# Install Kaggle command-line tool if not already installed
if ! command -v kaggle &> /dev/null; then
    echo "Installing Kaggle..."
    pip install kaggle
fi

# Check if the Kaggle API key is placed in the correct location
if [ ! -f ~/.kaggle/kaggle.json ]; then
    echo "Please place your Kaggle API key at ~/.kaggle/kaggle.json"
    exit 1
fi

# Create a virtual environment for the project
python3 -m venv bert_venv
source bert_venv/bin/activate

# Install Python dependencies
echo "Installing necessary Python packages..."
pip install transformers torch pandas numpy

# Create /input directory if it doesn't exist
mkdir -p ./input

# Download datasets into the /input directory
echo "Downloading datasets from Kaggle..."
kaggle datasets download -d abhishek/bert-base-uncased -p ./input/bert_base_uncased --unzip
kaggle datasets download -d abhinavwalia95/entity-annotated-corpus -p ./input/entity_annotated_corpus --unzip

echo "Setup complete!"

