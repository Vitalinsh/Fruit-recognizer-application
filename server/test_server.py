import unittest

from RequestValidator import getImageByteArray
from unittest.mock import MagicMock

class TestStringMethods(unittest.TestCase):

    def test_validationNoFile(self):

        files = {"imae" : 1}

        result = getImageByteArray(files)

        hasFile = result
        self.assertFalse(hasFile)

    def test_Validation(self):

        mockFile = MagicMock()
        mockFile.read.return_value = '010181312390'
        files = {"image" : mockFile}
        result = getImageByteArray(files)
        
        self.assertTrue(result[0])
        self.assertEqual(result[1],'010181312390')

if __name__ == '__main__':
    unittest.main()