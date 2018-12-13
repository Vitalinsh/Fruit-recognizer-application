import numpy as np
import cv2
import os

from keras.models import model_from_json
from keras.models import Sequential
from keras.layers import Dense, Flatten, Dropout, Conv2D, MaxPooling2D
from keras.regularizers import l2


class FruitRecognizer():
    """Class for classification fruit on the picture"""

    def __init__(self, model_path="saved_models/model1_vgg16_architecture.json",
                 weights_path="saved_models/model1_vgg16_best1_weights.hdf5", create_new_cnn=False):

        '''
		Parameters:
		model_path : json file path with the model to load.
		path_weigths : hdf5 file path with weights to load.
		'''
        if create_new_cnn:
            np.random.seed(5)
            self.model = Sequential()

            self.model.add(Conv2D(32, kernel_size=(3, 3), activation="relu", input_shape=(100, 100, 3)))
            self.model.add(Conv2D(64, (3, 3), activation="relu"))
            self.model.add(MaxPooling2D(pool_size=(2, 2)))

            self.model.add(Flatten())
            self.model.add(Dense(128, activation="relu"))
            self.model.add(Dropout(0.5))

            self.model.add(
                Dense(20, activation="softmax", kernel_initializer="glorot_uniform", kernel_regularizer=l2(0.01)))
            self.model.compile(loss='categorical_crossentropy', optimizer="adam", metrics=['accuracy'])

        else:
            with open(model_path, "r") as json_file:
                loaded_model = json_file.read()
            self.model = model_from_json(loaded_model)

            self.model.load_weights(weights_path)

            self.model.compile(loss="categorical_crossentropy", optimizer="adam", metrics=["accuracy"])

    def predict(self, image, return_probs=False):
        """
		Parameters:
		image : ndarray of shape like (100, 100, 3)
			Represents 3-channel picture.
		
		Returns:
		predict : int, if return_probs == False
				ndarray of class probabilities, if return_probs == True
		"""

        classes = ['Apple Red Yellow', 'Apple Golden 1', 'Avocado', 'Avocado ripe', 'Banana', 'Cocos', 'Dates',
                   'Granadilla', 'Grape Pink', 'Grape White', 'Kiwi', 'Kumquats', 'Lemon', 'Lemon Meyer', 'Limes',
                   'Nectarine', 'Orange', 'Peach', 'Peach Flat', 'Apricot']

        image, squared_img = self.img_preprocessing(image)

        if return_probs:
            predict = self.model.predict(image)
        else:
            predict = classes[np.argmax(self.model.predict(image))]

        if squared_img or return_probs:
            return predict
        else:
            return predict + "Image isn't square! Prediction may be incorrect!"

    def img_preprocessing(self, image):
        '''
		method to preprocess image for prediction.
		
		Parameters:
		image : ndarray of shape (-, -, 3)
		
		Returns:
		ready_img : ndarray of shape (1, 100, 100, 3)
		squared_img : Boolean
		'''
        squared_img = image.shape[0] == image.shape[1]
        image = cv2.resize(image, (100, 100))
        image = np.expand_dims(image, axis=0)
        image = image / 255

        return image, squared_img
