import requests
import json
import cv2

addr = 'http://localhost:5000'

test_url = addr + '/api/recognize'

# prepare headers for http request
content_type = 'image/jpeg'
headers = {'content-type': content_type}


with open('lena.jpg','rb') as f:
    response = requests.post(test_url, files={'image': f})

#img = cv2.imread('lena.jpg')
# encode image as jpe
#_, img_encoded = cv2.imencode('.jpg', img)
# send http request with image and receive response
#response = requests.post(test_url, data=img_encoded.tostring(), headers=headers)
# decode response
print (json.loads(response.text))

# expected output: {u'message': u'image received. size=124x124'}