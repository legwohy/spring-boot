class PersonDTO:

    _name = '小明'# 私有
    age = 10 # 共有
    food = 'tomar'

    def doCokkie(self,name,age):
        self.name = name
        self.age = age
        print('name:'+name+",age:"+str(age)+",do:"+self.food)


wang = PersonDTO()
wang.doCokkie('小雷',10)