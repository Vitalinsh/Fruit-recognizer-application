import json
import sys

from pytest_bdd import scenario, given, when, then
import pytest

from config_tests import config
sys.path.append(config.server_dir)
from server import app

status, result = "", ""


@pytest.fixture
def app_server():
    return app


@pytest.fixture
def picture_path():
    picture_name = "lena.jpg"
    return picture_name


@scenario("api.feature", "File not found or is not a picture")
def test_api():
    pass


@given("server is working")
def running_server(app_server):
    app_server.config['TESTING'] = True
    app_server = app_server.test_client()
    return app_server


@when("user send request")
def send_file(picture_path, running_server):
    global status, result
    response = running_server.post("/api/recognize", data=dict(message="text"))
    result = json.loads(response.data)
    status = response.status_code


@then("return message 'No file received'")
def classification_result():
    global status, result
    text = result["message"]

    assert(status == 200)
    assert(text == "No file received")

