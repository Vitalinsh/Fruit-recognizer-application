import json

import requests

addr = 'http://localhost:5000'

test_url = addr + '/api/recognize'

# prepare headers for http request
content_type = 'image/jpeg'
headers = {'content-type': content_type}

with open('granadilla.jpg', 'rb') as f:
    response = requests.post(test_url, files={'image': f})

print(json.loads(response.text))
print(type(json.loads(response.text)))
input("Press any key to exit...")

# r = requests.post("http://localhost:5000/shutdown")
# print(r)
# print(r.text)


