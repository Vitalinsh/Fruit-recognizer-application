import json

import requests
import cv2

addr = 'http://localhost:5000'
test_url = addr + '/api/recognize_fruit'

# prepare headers for http request
content_type = 'image/jpeg'
headers = {'content-type': content_type}

img = cv2.imread('lena.jpg')
# encode image as jpe
_, img_encoded = cv2.imencode('.jpg', img)
response = requests.post(test_url, data=img_encoded.tostring(),
                         headers=headers)

print(json.loads(response.text))
# expected output: {u'message': u'image received. size=124x124'}
input("Press any key to exit...")

