import re
from flask import Flask
from flask import render_template
from flask import request
import pandas as pd 
import math
import pickle as pk
from sklearn.preprocessing import PolynomialFeatures
import numpy as np


app=Flask(__name__)
poly_features = PolynomialFeatures(degree=6)
places=pd.read_csv('boston.csv')



model=pk.load(open('modell.pkl','rb'))


@app.route('/about')
def about():

     return render_template('about.html')

@app.route('/')
@app.route('/predict',methods=['POST','GET'])
def index():

    sources=places.iloc[:,0]
    dist=places.iloc[:,0]

    if request.method=='POST':
        
        so=request.form.get('source')
        dis=request.form.get('dist')
        
        lat1=float(places[places['3rd Place  212']==so].iloc[:,1])
        lat2=float(places[places['3rd Place  212']==dis].iloc[:,1])
        long1=float(places[places['3rd Place  212']==so].iloc[:,2])
        long2=float(places[places['3rd Place  212']==dis].iloc[:,2])
        
        distance=distance_calculation(lat1,lat2,long1,long2)

        print(distance)

        uber_pool=get_price(distance,1)
        taxi=get_price(distance,2)
        uberx=get_price(distance,3)
        uberxl=get_price(distance,4)
        black=get_price(distance,6)
        black_suv=get_price(distance,7)
        

        return render_template('base.html',sources=sources,dist=dist,uberpool_price=uber_pool,taxi_price=taxi,uberx_price=uberx,uberxl_price=uberxl,black_price=black,black_suv_price=black_suv)

    else:
        return render_template('base.html',sources=sources,dist=dist)        
    

def get_price(distance,i):
    
    inputquary_uberpool=np.array([[float(distance),float(1),float(0),float(i),float(1)]])
    inputquary_uberpool=poly_features.fit_transform(inputquary_uberpool)
    result_uberpool=model.predict(inputquary_uberpool)[0]

    result_uberpool="$"+"{:.2f}".format(result_uberpool)+"-"+"$"+"{:.2f}".format(result_uberpool+3.0)

    return result_uberpool


def distance_calculation(lat1,lat2,long1,long2):
    lat1=math.radians(lat1)
    lat2=math.radians(lat2)
    long1=math.radians(long1)
    long2=math.radians(long2)
    
    dlon=long2-long1
    dlat=lat2-lat1
    a=math.pow(math.sin(dlat/2),2)+math.cos(lat1)*math.cos(lat2)*math.pow(math.sin(dlon/2),2)
    
    c=2*math.asin(math.sqrt(a))
    
    r=6371
    
    return (c*r)



if __name__=='__main__':
    app.run(debug=True)