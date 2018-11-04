import unittest
import sys
import os
import numpy as np

sys.path.append("models")
import recognizer
 
 
class RecognizerTest(unittest.TestCase):
	def test_predict_1(self):
		model =  recognizer.FruitRecognizer(model_path="models\saved_models\model1_vgg16_architecture.json",
 					            weights_path="models\saved_models\model1_vgg16_best1_weights.hdf5")
		
		image = np.zeros((100, 100, 3))
		y = model.predict(image) 
		print(type(y))
		self.assertEqual(type(y), np.int64)
        
        
if __name__ == '__main__':
	unittest.main()