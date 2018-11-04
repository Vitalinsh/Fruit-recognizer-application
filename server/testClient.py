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

print (json.loads(response.text))

