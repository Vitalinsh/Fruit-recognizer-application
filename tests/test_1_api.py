import json

from pytest_bdd import scenario, given, when, then
import pytest

from server import app

status, result = "", ""


@pytest.fixture
def app_server():
    return app


@pytest.fixture
def picture_path():
    picture_name = "granadilla.jpg"
    return picture_name


@scenario("api.feature", "Recognize a fruit")
def test_api():
    pass


@given("server is working")
def running_server(app_server):
    app_server.config['TESTING'] = True
    app_server = app_server.test_client()
    return app_server


@when("user send picture")
def send_file(picture_path, running_server):
    global status, result
    with open(picture_path, 'rb') as inf:
        response = running_server.post("/api/recognize", data=dict(image=inf))
    result = json.loads(response.data)
    status = response.status_code


@then("return classification result")
def classification_result():
    global status, result
    text = result["message"]

    assert(status == 200)
    assert(text != "No file received")
    print(text)


