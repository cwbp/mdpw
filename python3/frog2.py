import mdpw
from mdpw import note as n
from mdpw import seq as s
from mdpw import chord as c
from mdpw import compile

m0 = s(n(64,1,64,17), n(66), n(68), n(69), n(68),n(66),n(64),n(0,1,0))
m01 = s(n(64,.5,64,17),n(64), n(66), n(66), n(68),n(68), n(69),n(69), n(68),n(68),n(66),n(66),n(64,1),n(0,1,0))
m1 = s(n(68,1,64,17), n(69), n(71), n(73), n(71),n(69),n(68),n(0,1,0))
m2 = s(n(64,2,125,17), n(64), n(64), n(64))
a = s(m0, m1, m2,m01)
r = n(0,8,0,1)
a =c(a, s(r, a), s(r, r, a))

mdpw.compile(a, 120).write("frog.mid")
