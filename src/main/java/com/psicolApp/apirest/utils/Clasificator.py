from transformers import pipeline

# Crear el pipeline de análisis de sentimiento
sentiment_pipeline = pipeline("sentiment-analysis", model="distilbert-base-uncased-finetuned-sst-2-english")

def evaluate_note_content(content):
    result = sentiment_pipeline(content)
    return result

if __name__ == "__main__":
    import sys
    if len(sys.argv) != 2:
        print("Usage: python script.py '<note_content>'")
        sys.exit(1)
    
    note_content = sys.argv[1]
    evaluation_result = evaluate_note_content(note_content)
    print(evaluation_result)
