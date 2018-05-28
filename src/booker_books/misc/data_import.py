import requests
import json

with open('../../../authors.json') as f:
  data = json.load(f)

r = requests.post('http://localhost:4000/login', {'email': 'james@gmail.com', 'password': 'password'})

token = json.loads(r.text)['token']
headers = {'Authorization': "bearer {}".format(token)}

# import code; code.interact(local=dict(globals(), **locals())a
for author in data:
  requests.post('http://localhost:3000/author', json = {
    'name': author['name'],
    'surname': author['surname'],
    'description': author['description'],
    'birthDate': author['birthDate']
  }, headers=headers)  