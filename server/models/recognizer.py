import numpy as np
import cv2
import os
import tensorflow
from keras.models import model_from_json
from keras import backend as K

class FruitRecognizer():
    """Class for classification fruit on the picture"""
    
    def __init__(self, model_path="saved_models\model1_vgg16_architecture.json",
                 weights_path="saved_models\model1_vgg16_best1_weights.hdf5"):
        """
        Parameters:
        model_path : json file path with the model to load.
        path_weigths : hdf5 file path with weights to load.
        """

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
        image = cv2.resize(image, (100, 100))
        image = np.expand_dims(image, axis=0) / 255

        if return_probs:
            predict = self.model.predict(image)

        else:
            predict = np.argmax(self.model.predict(image))
        
        return predict
        