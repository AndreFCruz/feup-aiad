import sys
import pandas

if __name__ == '__main__':
    if len(sys.argv) < 2:
        print("Usage: python %s <file1 [file2 [file3 ...]]>" % sys.argv[0])
        sys.exit(1)

    df = None
    for file_path in sys.argv[1:]:
        if df is None:
            df = pandas.read_csv(file_path, header=0, index_col=0)
        else:
            df.append(pandas.read_csv(file_path, header=0, index_col=0))

        import ipdb; ipdb.set_trace()


    df.to_csv('joined_datasets.csv')
