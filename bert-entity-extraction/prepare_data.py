import pandas as pd
from transformers import BertTokenizer

MAX_LEN = 128
BASE_MODEL_PATH = "./input/bert_base_uncased"
TOKENIZER = BertTokenizer.from_pretrained(BASE_MODEL_PATH, do_lower_case=True)

# Load and process data
def load_data(file_path):
    data = pd.read_csv(file_path, encoding='latin1')
    data = data.fillna(method="ffill")
    return data

# Tokenize and encode the dataset
def prepare_data_for_bert(data):
    input_ids, attention_masks, token_type_ids, labels = [], [], [], []
    
    for idx, row in data.iterrows():
        sentence = row['Sentence']
        label = row['Label']

        # Tokenize and encode inputs
        inputs = TOKENIZER(
            sentence,
            add_special_tokens=True,
            max_length=MAX_LEN,
            padding='max_length',
            truncation=True,
            return_tensors="pt"
        )
        
        input_ids.append(inputs["input_ids"].squeeze().tolist())
        attention_masks.append(inputs["attention_mask"].squeeze().tolist())
        token_type_ids.append(inputs["token_type_ids"].squeeze().tolist())
        labels.append(label)
    
    return input_ids, attention_masks, token_type_ids, labels

# Example usage
if __name__ == "__main__":
    entity_annotated_corpus_path = './input/entity_annotated_corpus/ner_dataset.csv'
    data = load_data(entity_annotated_corpus_path)
    input_ids, attention_masks, token_type_ids, labels = prepare_data_for_bert(data)
    print("Data prepared for BERT!")
