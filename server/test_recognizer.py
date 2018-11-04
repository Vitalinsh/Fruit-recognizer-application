import unittest
from unittest.mock import MagicMock
import sys
import os
import numpy as np

from models import recognizer

 
class RecognizerTest(unittest.TestCase):

	def test_1_predict_type(self):
		'''
		Test with using MagicMock
		'''	
		model_path = os.path.join("model1_vgg16_architecture.json")
		weights_path = os.path.join("model1_vgg16_best1_weights.hdf5")
		
		model =  recognizer.FruitRecognizer(model_path="model1_vgg16_architecture.json",
											weights_path="model1_vgg16_best1_weights.hdf5")
		
		image = np.ones((100, 100, 3)) * 111
		model.img_preprocessing = MagicMock(return_value=[np.ones((1, 100, 100, 3)), True])
		
		y = model.predict(image) 
		self.assertEqual(type(y), str)
		
		
	def test_2_several_models(self):
		'''
		Test with using MagicMock
		'''	
		model_path = os.path.join("models", "saved_models", "model1_vgg16_architecture.json")
		weights_path = os.path.join("models", "saved_models", "model1_vgg16_best1_weights.hdf5")
		
		image = np.ones((100, 100, 3)) * 200	
		y = []
		for i in range(4):
			model = recognizer.FruitRecognizer(model_path="model1_vgg16_architecture.json",
											weights_path="model1_vgg16_best1_weights.hdf5") 
											
			model.img_preprocessing = MagicMock(return_value=[np.ones((1, 100, 100, 3)), True])
			y.append(model.predict(image))
								
		self.assertEqual(y[0], y[1])
		self.assertEqual(y[2], y[3])
		self.assertEqual(y[0], y[3])
		
	def test_3_preprocessing(self):

		model_path = os.path.join("models", "saved_models", "model1_vgg16_architecture.json")
		weights_path = os.path.join("models", "saved_models", "model1_vgg16_best1_weights.hdf5")
		
		image = np.ones((2000, 1000, 3)) * 200
		model =  recognizer.FruitRecognizer(model_path="model1_vgg16_architecture.json",
											weights_path="model1_vgg16_best1_weights.hdf5")
		
		clear_img, squared_img = model.img_preprocessing(image)
		
		self.assertEqual(squared_img, False)
		self.assertEqual(type(clear_img), np.ndarray)
		self.assertEqual(clear_img.shape, (1, 100, 100, 3))
        
        
if __name__ == '__main__':
	print("Current: ", os.listdir())
	print("Models: ", os.listdir("models"))
	print("Saved:", os.listdir("models/saved_models"))
	unittest.main()