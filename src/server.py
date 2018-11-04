import subprocess

from flask import Flask, request, Response
import jsonpickle
import numpy as np
import cv2

from src.models.recognizer import FruitRecognizer

#version = subprocess.check_output(["git", "describe"]).strip()
app = Flask(__name__)

# route http posts to this method
@app.route('/api/test', methods=['POST'])
def test():
    r = request
    # convert string of image data to uint8
    nparr = np.fromstring(r.data, np.uint8)

    # decode image
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # do some fancy processing here....

    reco = FruitRecognizer(model_path="models\saved_models\model1_vgg16_architecture.json",
                           weights_path="models\saved_models\model1_vgg16_best1_weights.hdf5")

    res = reco.predict(img)

    # build a response dict to send back to client
    response = {'message': 'Your fruict is {}'.format(res)
                }
    # encode response using jsonpickle
    response_pickled = jsonpickle.encode(response)

    return Response(response=response_pickled, status=200, mimetype="application/json")


# start flask app
app.run(host="0.0.0.0", port=5000)