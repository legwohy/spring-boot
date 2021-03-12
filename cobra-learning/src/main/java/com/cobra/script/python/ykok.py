import unittest,requests,json,HTMLTestRunner
from datafile import data1
from ddt import ddt,data,unpack
@ddt
class te_lo(unittest.TestCase):
    heaeder={}
    def test1(self):
        ure1="http://mobile.bwstudent.com/movieApi/user/v2/login"
        qq1="POST"
        da1={"email":"525776038@qq.com","pwd":"ms8o3XtsprzAP8Da489S4g=="}
        re1=requests.request(qq1,ure1,data=da1)
        self.heaeder["userId"] = str(re1.json()["result"]["userId"])
        self.heaeder["sessionId"] = re1.json()["result"]["sessionId"]
        print(re1.text)
    def test2(self):
        ure2 = "http://mobile.bwstudent.com/movieApi/cinema/v1/verify/followCinema"
        qq2 = "GET"
        daa2 = {"cinemaId": "24"}
        re2 = requests.request(qq2,ure2,headers=self.heaeder,params=daa2)
        print(re2.text)
    def test3(self):
        ure3 = "http://mobile.bwstudent.com/movieApi/cinema/v1/verify/cancelFollowCinema"
        qq3 = "GET"
        daa3 = {"cinemaId": "24"}
        re3 = requests.request(qq3,ure3,headers=self.heaeder,params=daa3)
        print(re3.text)

    def test4(self):
        ure4 = "http://mobile.bwstudent.com/movieApi/cinema/v1/verify/cinemaComment"
        qq4 = "POST"
        daa4 = {"cinemaId": "1","commentContent":"地方"}
        re4 = requests.request(qq4, ure4, headers=self.heaeder, json=daa4,)
        print(re4.text)
        assert"1001" in re4.text
    @data(*data1)
    @unpack
    def test6(self,*b):
        u6=b[2]
        uu6=b[3]
        dat6=json.loads(b[4])
        r6=requests.request(uu6,u6,headers=self.heaeder,params=dat6)
        print(r6.text)
    if __name__ == '__main__':
        so=unittest.defaultTestLoader.discover("./","ykok.py")
        se=HTMLTestRunner.HTMLTestRunner(open("ko.html","wb")
                                         ,title="登录",description="执行3")
        se.run(so)