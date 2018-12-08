import sys
import pandas as pd
from ipdb import set_trace

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("Usage: python %s <file1>" % sys.argv[0])
        sys.exit(1)

    csv_path = sys.argv[1]
    df = pd.read_csv(csv_path)

    s = pd.Series(list(df['Unnamed: 0']), dtype="category")
    print(s.describe())
    set_trace()
