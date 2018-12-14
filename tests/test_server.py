import unittest
from unittest.mock import MagicMock

from RequestValidator import getImageByteArray


class TestStringMethods(unittest.TestCase):

    def test_validationNoFile(self):

        files = {"number": 1}

        result = getImageByteArray(files)

        hasFile = result[0]
        self.assertFalse(hasFile)

    def test_Validation(self):

        mockFile = MagicMock()
        mockFile.read.return_value = '010181312390'
        files = {"image": mockFile}
        result = getImageByteArray(files)

        self.assertTrue(result[0])
        self.assertEqual(result[1], '010181312390')


if __name__ == '__main__':
    unittest.main()
