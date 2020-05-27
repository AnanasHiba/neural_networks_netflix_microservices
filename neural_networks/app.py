from flask import Flask, Response
from model.db import initialize_db
from model.project import Project
import json
import py_eureka_client.eureka_client as eureka_client

app = Flask(__name__)

EUREKA_URL = "http://localhost:8761/eureka"
APP_NAME = "neural_networks"
SERVER_PORT = 8000
eureka_client.init(eureka_server=EUREKA_URL,
                   app_name=APP_NAME,
                   instance_port=SERVER_PORT,
                   ha_strategy=eureka_client.HA_STRATEGY_RANDOM)


app.config['MONGODB_SETTINGS'] = {
    'host': 'mongodb+srv://root:root1234@cluster0-edkji.mongodb.net/gallery?replicaSet=Cluster0-shard-0'
            '&readPreference=primary&connectTimeoutMS=10000&authSource=admin&ssl=true&ssl_cert_reqs=CERT_NONE'
            '&authMechanism=SCRAM-SHA-1',
}

initialize_db(app)


@app.route('/')
def hello_world():
    return 'Hello World!'


@app.route('/movies/<id>')
def calculate(id):
    project = Project.objects.get(_id=id).to_json()
    return Response(project, mimetype="application/json", status=200)


if __name__ == '__main__':
    app.run(port=SERVER_PORT)
