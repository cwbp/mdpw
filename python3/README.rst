MDPW
====
This package generates a PrettyMIDI object from Array of Array. 
Here is a sample code.
:: 

 import mdpw
 a = ['s',['n',64,1,64,1],['n',68,1,64,1],['n',71,1,64,1],
    ['c',['n',64,1,64,1],['n',68,1,64,1],['n',71,1,64,1]]]
 mdpw.compile(a, 120).write("hello.mid")


'n' means a note and followed values are pitch, duration, velocity and instrument. The instrument 129 means Drum.
's' means to play sequentially and 'c' means to play simultaneously.


Here is anther sample code.
::

 from mdpw import INSTRUMENTS as I
 from mdpw import DRUMS as D
 from mdpw import note, seq, chord, compile
 dph = D['PedalHi-Hat']
 i = I['Drum']
 hh = ['s',['n',dph,1,127,i],['n',dph,1],['n',dph,1],['n',dph,1]]
 b = seq(note(64,.5,0,I['ElectricBassPick']),note(64,1,127),note(64,1),note(64,1))
 m = chord(seq(hh,hh,hh,hh),seq(b,b,b,b))
 compile(m, 120).write("sample.mid")

In fact, the functions 'note', 'seq', 'chord' return arrays.

You can generate a round chant like this:
::

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




