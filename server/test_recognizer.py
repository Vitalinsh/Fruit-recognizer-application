import unittest
from unittest.mock import MagicMock
import sys
import os
import numpy as np

from models import recognizer

model_path = os.path.join("model1_vgg16_architecture.json")
weights_path = os.path.join("model1_vgg16_best1_weights.hdf5")
 
class RecognizerTest(unittest.TestCase):

	def test_1_predict_type(self):
		'''
		Test with using MagicMock
		'''	
		
		model =  recognizer.FruitRecognizer(model_path=model_path,
											weights_path=weights_path, 
											create_new_cnn=True)
		
		image = np.ones((100, 100, 3)) * 111
		model.img_preprocessing = MagicMock(return_value=[np.ones((1, 100, 100, 3)), True])
		
		y = model.predict(image) 
		self.assertEqual(type(y), str)
		
		
	def test_2_several_models(self):
		'''
		Test with using MagicMock
		'''		
		image = np.ones((100, 100, 3)) * 200	
		y = []
		for i in range(4):
			model = recognizer.FruitRecognizer(model_path=model_path,
												weights_path=weights_path,
												create_new_cnn=True) 
											
			model.img_preprocessing = MagicMock(return_value=[np.ones((1, 100, 100, 3)), True])
			y.append(model.predict(image))
								
		self.assertEqual(y[0], y[1])
		self.assertEqual(y[2], y[3])
		self.assertEqual(y[0], y[3])
		
	def test_3_preprocessing(self):
		
		image = np.ones((2000, 1000, 3)) * 200
		model =  recognizer.FruitRecognizer(model_path=model_path,
											weights_path=weights_path,
											create_new_cnn=True)
		
		clear_img, squared_img = model.img_preprocessing(image)
		
		self.assertEqual(squared_img, False)
		self.assertEqual(type(clear_img), np.ndarray)
		self.assertEqual(clear_img.shape, (1, 100, 100, 3))
        
        
if __name__ == '__main__':
	unittest.main()