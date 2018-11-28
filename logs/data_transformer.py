# from ipdb import set_trace
import sys

NUMBER_ENVIRONMENT_VARS = 11


def transform_data_A(filename):
    pass


def transform_data_B(filename):
    filename_env_vars = filename + "_env_vars"
    filename_data = filename + "_ticks_consumers"

    file = open(filename, "r")
    data = file.readlines()
    file.close()

    headers = data[0:NUMBER_ENVIRONMENT_VARS]
    file = open(filename_env_vars, "w")
    for elem in headers:
        file.write(elem)
    file.close()

    body = data[NUMBER_ENVIRONMENT_VARS+2:]
    file = open(filename_data, "w")
    for elem in body:
        file.write(elem)
    file.close()

    pass


if __name__ == "__main__":

    if len(sys.argv) != 3:
        print("Usage: python %s <filename> <scene: A/B>" % sys.argv[0])
        sys.exit(-1)

    filename = sys.argv[1]
    scene = sys.argv[2].upper()

    transform_data_A(filename) if scene == "A" else transform_data_B(filename)
