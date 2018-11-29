# from ipdb import set_trace
import sys

NUMBER_ENVIRONMENT_VARS = 11

class Contract:
    def __init__(self, ticks, price_per_unit, percent_renewable, distance):
        self.ticks = ticks
        self.price_per_unit = price_per_unit
        self.percent_renewable = percent_renewable
        self.distance = distance


def transform_data_A(filename, num_contracts_per_row=5):
    """
    Function to transform a file of new contracts into a training dataset.
    :param filename: file path for the file outputted by Sajas,
     to be transformed on a training dataset by this function.
    """
    file_in = open(filename, 'r')
    data = file_in.readlines()
    file_in.close()

    _, body = split_header_data(data)

    ## Ignore first column ("ticks" column)
    classes = body[0].split(',')[1:]

    contracts = [list() for _ in range(len(classes))]

    for line in body[1:]:
        split_line = line.rstrip().split(',')
        ticks = int(float(split_line[0]))
        for idx, c in enumerate(split_line[1:]):
            if len(c) == 0:
                continue
            split_c = c.split(' ')
            contracts[idx].append(Contract(
                ticks, float(split_c[0]), float(split_c[1]), float(split_c[2])
            ))

    # from ipdb import set_trace; set_trace()

    ## Data preparation
    ## Save one row for each N consumer contracts
    file_out = open(filename + '_transformed', 'w')

    ## TODO export contract data, N for each row, to a pandas DataFrame
    # train_data = 


def transform_data_B(filename):
    filename_env_vars = filename + "_env_vars"
    filename_data = filename + "_ticks_consumers"

    file = open(filename, "r")
    data = file.readlines()
    file.close()

    headers, body = split_header_data(data)
    file = open(filename_env_vars, "w")
    for elem in headers:
        file.write(elem)
    file.close()

    file = open(filename_data, "w")
    for elem in body:
        file.write(elem)
    file.close()

def split_header_data(data):
    return data[0:NUMBER_ENVIRONMENT_VARS], data[NUMBER_ENVIRONMENT_VARS+2:]


if __name__ == "__main__":

    if len(sys.argv) != 3:
        print("Usage: python %s <filename> <scene: A/B>" % sys.argv[0])
        sys.exit(-1)

    filename = sys.argv[1]
    scene = sys.argv[2].upper()

    transform_data_A(filename) if scene == "A" else transform_data_B(filename)
