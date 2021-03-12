# pip install requests
import requests

url = 'https://www.baidu.com/s'

data={'wd': 'flower'}

res = requests.get(url,params=data)

print(res.text)







