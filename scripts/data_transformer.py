import sys
import pandas
from collections import deque

NUM_ENV_VARS = 11
NUM_FEATURES_PER_CONTRACT   = 3

class Contract:
    def __init__(self, ticks, price_per_unit, percent_renewable, distance):
        self.ticks = ticks
        self.price_per_unit = price_per_unit
        self.percent_renewable = percent_renewable
        self.distance = distance


def transform_data_A(filename, num_contracts_per_row=5, batch_mode=False, use_rolling_rows=True):
    """
    Function to transform a file of new contracts into a training dataset.
    :param filename: file path for the file outputted by Sajas,
     to be transformed on a training dataset by this function.
    """
    file_in = open(filename, 'r')
    data = file_in.readlines()
    file_in.close()

    _, body = split_header_data(data)

    ## Ignore first column ("ticks" column) (and "run" column if in batch_mode)
    classes = body[0].split(',')[3 if batch_mode else 1:]
    classes = [clazz.replace('"','').strip() for clazz in classes]

    contracts = [list() for _ in range(len(classes))]

    for line in body[1:-2 if batch_mode else len(body)]: # from class names onwards
        split_line = line.rstrip().split(',')
        ticks = int(float(split_line[1 if batch_mode else 0]))
        for idx, c in enumerate(split_line[3 if batch_mode else 1:]):
            if len(c) == 0:
                continue
            split_c = c.split(' ')
            contracts[idx].append(Contract(
                ticks, float(split_c[0]), float(split_c[1]), float(split_c[2])
            ))

    ## Data preparation
    if use_rolling_rows:
        train_data = contracts_to_rolling_rows(contracts, classes, num_contracts_per_row)
    else:
        train_data = contracts_to_data_rows(contracts, classes, num_contracts_per_row)

    data_to_csv(train_data, filename + ('_rolling' if use_rolling_rows else '_even-split'))


def contracts_to_data_rows(contracts, classes, num_contracts_per_row):
    ## Save one row for each N consumer contracts
    train_data = {clazz: list() for clazz in classes}
    for consumer_idx in range(len(contracts)):
        consumer_class = classes[consumer_idx]
        current_entry = list()
        for contract in contracts[consumer_idx]:
            current_entry.extend([
                contract.price_per_unit,
                contract.percent_renewable,
                contract.distance
            ])
            if len(current_entry) == NUM_FEATURES_PER_CONTRACT * num_contracts_per_row:
                train_data[consumer_class].append(current_entry)
                current_entry = list()

    return train_data


def contracts_to_rolling_rows(contracts, classes, num_contracts_per_row):
    train_data = {clazz: list() for clazz in classes}
    for consumer_idx in range(len(contracts)):
        consumer_class = classes[consumer_idx]
        current_entry = deque()

        for contract in contracts[consumer_idx]:
            current_entry.extend([
                contract.price_per_unit,
                contract.percent_renewable,
                contract.distance
            ])
            
            if len(current_entry) == NUM_FEATURES_PER_CONTRACT * num_contracts_per_row:
                train_data[consumer_class].append(list(current_entry))
                
                for _ in range(NUM_FEATURES_PER_CONTRACT):
                    current_entry.popleft()

    return train_data


def data_to_csv(train_data, filename):
    ## Label training data
    df = pandas.DataFrame()
    for clazz in train_data:
        rows = train_data[clazz]
        new_df = pandas.DataFrame(data=rows, index=[clazz for _ in range(len(rows))])
        df = df.append(new_df)


    ## Shuffle rows
    df = df.sample(frac=1)

    ## Export data to file
    df.to_csv(filename + '.csv')


def split_header_data(data):
    return data[0:NUM_ENV_VARS], data[NUM_ENV_VARS+2:]


if __name__ == "__main__":

    if len(sys.argv) < 3 or len(sys.argv) > 4:
        print("Usage: python %s <filename> <batch_mode: 1/0> [use_rolling_rows: 1/0]" % sys.argv[0])
        sys.exit(1)

    filename = sys.argv[1]
    batch_mode = True if int(sys.argv[2]) == 1 else False
    use_rolling_rows = True if (len(sys.argv) == 4 and int(sys.argv[3]) == 1) else False
    print('Running script ' + ('in NOT' if not batch_mode else 'IN') + ' batch mode.')

    transform_data_A(filename, batch_mode=batch_mode, use_rolling_rows=use_rolling_rows)

