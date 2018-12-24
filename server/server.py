import subprocess
import sys

from flask import Flask, request, Response
import jsonpickle
import numpy as np
import cv2
import tensorflow as tf
import matplotlib.pyplot as plt

from config_server import config
sys.path.append(config.project_dir)
from models.recognizer import FruitRecognizer
from RequestValidator import getImageByteArray

global graph
graph = tf.get_default_graph()

version = subprocess.check_output(["git", "describe"]).strip()
print(version)

reco = FruitRecognizer()
app = Flask(__name__)


@app.route('/', methods=['GET'])
def main_page():
    return "This is fruit recognizer API."


@app.route('/main', methods=['GET'])
def main_page2():
    return "This is fruit recognizer API."


# route http posts to this method
@app.route('/api/recognize', methods=['POST'])
def test():

    fileReceived = getImageByteArray(request.files)
    if not fileReceived[0]:
        response = {'message': 'No file received'}
        response_pickled = jsonpickle.encode(response)
        return Response(response=response_pickled, status=200,
                        mimetype="application/json")

    image = np.asarray(bytearray(fileReceived[1]), dtype="uint8")
    image = cv2.imdecode(image, cv2.IMREAD_COLOR)
    image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)
    # plt.imshow(image)
    # plt.show()
    with graph.as_default():
        res = reco.predict(image)

    # build a response dict to send back to client
    response = {'message': 'Your fruit is {}'.format(res)}
    response_pickled = jsonpickle.encode(response)
    return Response(response=response_pickled, status=200,
                    mimetype="application/json")


#def shutdown_server():
#    func = request.environ.get('werkzeug.server.shutdown')
#    if func is None:
#        raise RuntimeError('Not running with the Werkzeug Server')
#    func()


#@app.route('/shutdown', methods=['POST'])
#def shutdown():
#    shutdown_server()
#    return 'Server shutting down...'


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000, debug=False, threaded=False)
