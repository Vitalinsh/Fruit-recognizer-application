import json
import time
import matplotlib.pyplot as plt
from PIL import Image

import requests

img = Image.open('granadilla.jpg')
plt.imshow(img)
plt.show()

addr = 'http://localhost:5000'
test_url = addr + '/api/recognize'
# prepare headers for http request
# content_type = 'image/jpeg'
# headers = {'content-type': content_type}

print("start request...")
start = time.time()
with open('granadilla.jpg', 'rb') as f:
    response = requests.post(test_url, files={'image': f})

print(json.loads(response.text)["message"])
# print(type(json.loads(response.text)))
print("Classification time =", time.time() - start)
input("Press any key to exit...")

# r = requests.post("http://localhost:5000/shutdown")
# print(r)
# print(r.text)


