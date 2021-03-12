# pip install requests
import requests

url = 'http://172.17.36.28:8030/api/csvConfig/queryByPrimaryKey'

data={'id': 115, 'sessionKey': 'd06d1b5a-a60e-475f-9bba-a3f0b4aaea38'}

res = requests.post(url,json=data)
if(res.status_code==200):
    print(res.text)
else:
    print("异常")







