import subprocess

from flask import Flask, request, Response
import jsonpickle
import numpy as np
import cv2
import tensorflow as tf
from models.recognizer import FruitRecognizer

from RequestValidator import getImageByteArray

global graph
graph = tf.get_default_graph()

version = subprocess.check_output(["git", "describe"]).strip()
print(version)

reco = FruitRecognizer(model_path="models\saved_models\model1_vgg16_architecture.json",
                       weights_path="models\saved_models\model1_vgg16_best1_weights.hdf5")

app = Flask(__name__)



# route http posts to this method
@app.route('/api/recognize', methods=['POST'])
def test():

    fileReceived = getImageByteArray(request.files)

    if not (fileReceived[0]):
        response = {'message': 'No file received'}
        response_pickled = jsonpickle.encode(response)
        return Response(response=response_pickled, status=200, mimetype="application/json")

    image = np.asarray(bytearray(fileReceived[1]), dtype="uint8")
    image = cv2.imdecode(image, cv2.IMREAD_COLOR)

    with graph.as_default():
     res = reco.predict(image)

    # build a response dict to send back to client
    response = {'message': 'Your fruit is {}'.format(res)}

    response_pickled = jsonpickle.encode(response)

    return Response(response=response_pickled, status=200, mimetype="application/json")

# start flask app

app.run(host='0.0.0.0', debug=True, threaded=False)