from .db import db


class Project(db.Document):
    _id = db.StringField(required=True)
    title = db.StringField(required=True)
    accountName = db.StringField(required=True)
    description = db.StringField(required=True)
    _class = db.StringField()
    meta = {'collection': 'projects'}
