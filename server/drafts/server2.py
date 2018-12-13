import subprocess
import os

from flask import Flask, request, Response
import jsonpickle
import numpy as np
import cv2
import tensorflow as tf

from models.recognizer import FruitRecognizer

global graph
graph = tf.get_default_graph()

base_dir = os.path.abspath(os.path.dirname(__file__))
model_path = os.path.join(base_dir, 'models', "saved_models",
                          "model1_vgg16_architecture.json")
weights_path = os.path.join(base_dir, 'models', "saved_models",
                            "model1_vgg16_best1_weights.hdf5")

version = subprocess.check_output(["git", "describe"]).strip()
print(version)

reco = FruitRecognizer(model_path=model_path,
                       weights_path=weights_path)
app = Flask(__name__)


# route http posts to this method
@app.route('/api/recognize_fruit', methods=['POST'])
def test():
    r = request
    # convert string of image data to uint8
    nparr = np.fromstring(r.data, np.uint8)

    # decode image
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)

    # do some fancy processing here....
    with graph.as_default():
        res = reco.predict(img)

    # build a response dict to send back to client
    response = {'message': 'Your fruit is {}'.format(res)}
    # encode response using jsonpickle
    response_pickled = jsonpickle.encode(response)

    return Response(response=response_pickled, status=200,
                    mimetype="application/json")


# start flask app
app.run(host="0.0.0.0", port=5000, debug=False)
# app.run(host='0.0.0.0', debug=False, threaded=False)
