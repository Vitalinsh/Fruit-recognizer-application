import os

import numpy as np
import cv2
from google_drive_downloader import GoogleDriveDownloader as gdd
from keras.models import model_from_json
from keras.models import Sequential
from keras.layers import Dense, Flatten, Dropout, Conv2D, MaxPooling2D
from keras.regularizers import l2

package_dir = os.path.abspath(os.path.dirname(__file__))
saved_dir = os.path.join(package_dir, "saved_models")
if not os.path.exists(saved_dir):
    os.makedirs(saved_dir)

model_path = os.path.join(saved_dir, "model2.4_90classes_architecture.json")
weights_path = os.path.join(saved_dir, "model2.4_90classes.hdf5")

class FruitRecognizer:
    """Class for classification fruit on the picture.
    Model 2.4 supports 90 classes.
    """

    def __init__(self,
                 model_path=model_path,
                 weights_path=weights_path,
                 create_new_cnn=False):
        """
        Parameters:
        model_path : json file path with the model to load.
        path_weigths : hdf5 file path with weights to load.
        """
        if create_new_cnn:
            np.random.seed(5)
            self.model = Sequential()

            self.model.add(Conv2D(32, kernel_size=(3, 3), activation="relu",
                                  input_shape=(100, 100, 3)))
            self.model.add(Conv2D(64, (3, 3), activation="relu"))
            self.model.add(MaxPooling2D(pool_size=(2, 2)))

            self.model.add(Flatten())
            self.model.add(Dense(128, activation="relu"))
            self.model.add(Dropout(0.5))

            self.model.add(Dense(20, activation="softmax",
                                 kernel_initializer="glorot_uniform",
                                 kernel_regularizer=l2(0.01)))
            self.model.compile(loss='categorical_crossentropy',
                               optimizer="adam", metrics=['accuracy'])

        else:
            if not os.path.exists(model_path):
                self.load_from_cloud(model_path, weights_path)

            with open(model_path, "r") as json_file:
                loaded_model = json_file.read()
            self.model = model_from_json(loaded_model)
            self.model.load_weights(weights_path)
            self.model.compile(loss="categorical_crossentropy",
                               optimizer="adam", metrics=["accuracy"])

    def predict(self, image, return_probs=False):
        """
        Parameters:
        image : array of shape like (100, 100, 3)
                Represents 3-channel picture.

        Returns:
        predict : int, if return_probs == False
                ndarray of class probabilities, if return_probs == True
        """
        classes = ['Apple Braeburn', 'Apple Golden 1', 'Apple Golden 2',
                   'Apple Golden 3', 'Apple Granny Smith', 'Apple Red 1',
                   'Apple Red 2', 'Apple Red 3', 'Apple Red Delicious',
                   'Apple Red Yellow 1', 'Apple Red Yellow 2', 'Apricot',
                   'Avocado', 'Avocado ripe', 'Banana', 'Banana Lady Finger',
                   'Banana Red', 'Cactus fruit', 'Cantaloupe 1',
                   'Cantaloupe 2', 'Carambula', 'Cherry 1', 'Cherry 2',
                   'Cherry Rainier', 'Cherry Wax Black', 'Cherry Wax Red',
                   'Cherry Wax Yellow', 'Chestnut', 'Clementine', 'Cocos',
                   'Dates', 'Granadilla', 'Grape Blue', 'Grape Pink',
                   'Grape White', 'Grape White 2', 'Grape White 3',
                   'Grape White 4', 'Grapefruit Pink', 'Grapefruit White',
                   'Guava', 'Huckleberry', 'Kaki', 'Kiwi', 'Kumquats',
                   'Lemon', 'Lemon Meyer', 'Limes', 'Lychee', 'Mandarine',
                   'Mango', 'Mangostan', 'Maracuja', 'Melon Piel de Sapo',
                   'Mulberry', 'Nectarine', 'Orange', 'Papaya',
                   'Passion Fruit', 'Peach', 'Peach 2', 'Peach Flat', 'Pear',
                   'Pear Abate', 'Pear Monster', 'Pear Williams', 'Pepino',
                   'Physalis', 'Physalis with Husk', 'Pineapple',
                   'Pineapple Mini', 'Pitahaya Red', 'Plum', 'Pomegranate',
                   'Quince', 'Rambutan', 'Raspberry', 'Redcurrant', 'Salak',
                   'Strawberry', 'Strawberry Wedge', 'Tamarillo', 'Tangelo',
                   'Tomato 1', 'Tomato 2', 'Tomato 3', 'Tomato 4',
                   'Tomato Cherry Red', 'Tomato Maroon', 'Walnut']

        image, squared_img = self.img_preprocessing(image)

        if return_probs:
            predict = self.model.predict_proba(image)
        else:
            probas = self.model.predict_proba(image)
            probas = np.squeeze(probas)

            predict = zip(probas, classes)
            predict = sorted(predict, key=lambda x: x[0])
            predict = list(reversed(predict))
            predict = predict[: 3]
            # Create predict string for output with probabilities
            predict = ["\n%s - %.2f%%" % (x[1], x[0]*100) for x in predict]
            predict = " ".join(predict)
            # predict = classes[np.argmax(self.model.predict(image))]

        if squared_img or return_probs:
            return predict
        else:
            return predict + "\nImage isn't square! " \
                             "Prediction may be incorrect!"

    def img_preprocessing(self, image):
        """
        method to preprocess image for prediction.

        Parameters:
        image : ndarray of shape (-, -, 3)

        Returns:
        ready_img : ndarray of shape (1, 100, 100, 3)
        squared_img : Boolean
        """
        squared_img = image.shape[0] == image.shape[1]
        image = cv2.resize(image, (100, 100))
        image = np.expand_dims(image, axis=0)
        image = image / 255

        return image, squared_img

    def load_from_cloud(self, model_load_path, weights_load_path):
        gdd.download_file_from_google_drive(
                                file_id='1vhDSkVIQd0Xx1wdjuHAUtRyWhAxmGBa9',
                                dest_path=model_load_path,
                                unzip=False)

        gdd.download_file_from_google_drive(
                                file_id='1ol6yU0YaL9_T5YHlQyLWmENUE74Jxq-c',
                                dest_path=weights_load_path,
                                unzip=False)
        print("Model loaded from cloud")


