from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier
from sklearn.metrics import accuracy_score, classification_report, log_loss, hamming_loss, f1_score
from sklearn.metrics import recall_score
from sklearn.metrics import precision_score
from sklearn.metrics import confusion_matrix
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import StratifiedShuffleSplit
from pprint import pprint

import pandas as pd
import numpy as np

PERCENTAGE_TRAINING = 70

""" one-hot encodes an integer array """
def integer_array_to_categorical(arr, n_classes):
    categorical = [np.zeros(shape=n_classes) for _ in arr]
    for i in range(len(arr)):
        categorical[i][arr[i]] = 1
    return categorical

""" fetch the dataset to use in prediction """
def get_aiad_dataset():
    path_to_csv = "../logs/sceneA___rolling_columns_joined.csv"
    df = pd.read_csv(path_to_csv)
    y = df["Unnamed: 0"].values
    x = df.drop(["Unnamed: 0"], axis=1).values

    le = LabelEncoder()
    y = le.fit_transform(y)

    stratified_splitter = StratifiedShuffleSplit(n_splits=10, train_size=float(PERCENTAGE_TRAINING / 100)
                                                 # , random_state=100
                                                 )

    _classes = [i for i in range(3)]

    _x_train, _y_train, _x_test, _y_test = None, None, None, None
    for train_index, test_index in stratified_splitter.split(x, y):
        _x_train = x[train_index]
        _y_train = y[train_index]
        _x_test = x[test_index]
        _y_test = y[test_index]

    return _classes, _x_train, _y_train, _x_test, _y_test


def get_random_forest_prediction(x_train, y_train, x_test):
    rf = RandomForestClassifier(
        n_estimators=100,
        criterion='gini',
        max_depth=100,
        max_features=None,
        n_jobs=2,
        verbose=2
    )

    rf.fit(x_train, y_train)

    return list(rf.predict(x_test))


def get_gradient_boosting_prediction(x_train, y_train, x_test):
    gb = GradientBoostingClassifier(
        loss='deviance',
        learning_rate=0.01,
        n_estimators=5,
        max_depth=100,
        max_features=None,
        criterion='mse',
        verbose=2
    )

    gb.fit(x_train, y_train)

    return list(gb.predict(x_test))


if __name__ == "__main__":

    classes, x_train, y_train, x_test, y_test = get_aiad_dataset()

    y_pred = get_random_forest_prediction(x_train, y_train, x_test)
    # y_pred = get_gradient_boosting_prediction(x_train, y_train, x_test)

    json_data = {
        'acc': accuracy_score(y_pred, y_test),
        'precision': precision_score(y_test, y_pred, labels=classes, average='weighted'),
        'recall': recall_score(y_test, y_pred, labels=classes, average='weighted'),
        'f1score': f1_score(y_test, y_pred, labels=classes, average='weighted'),
        'lloss': log_loss(y_test, integer_array_to_categorical(y_pred, len(classes)), labels=classes),
        'hloss': hamming_loss(y_pred, y_test, labels=classes),
        'conf_matrix': confusion_matrix(y_test, y_pred).tolist()
    }

    pprint(json_data)
