from flask import Flask,request,jsonify
from sklearn.preprocessing import PolynomialFeatures
import pickle
import numpy as np


poly_features = PolynomialFeatures(degree=6)

model=pickle.load(open("modell.pkl",'rb'))
# taxi-priceprediction

app=Flask(__name__)
@app.route('/')
def home():
    return "hello world"

@app.route('/predict',methods=['POST'])
def predict():
    # distance surge_multiplier	Lyft_Type	Uber_Type	cab_type_Uber
    distance=request.form.get("dist")
    surge_multiplier =request.form.get("surge")
    Lyft_Type=request.form.get("Lyft_Type")
    Uber_Type= request.form.get("Uber_Type")

    cab_type_Uber= request.form.get("cab_type_Uber")

    inputquary=np.array([[float(distance),float(surge_multiplier),float(Lyft_Type),float(Uber_Type),float(cab_type_Uber)]])

    inputquary=poly_features.fit_transform(inputquary)

    result=model.predict(inputquary)[0]

    # result={'distance':distance,"surge_multiplier":surge_multiplier,"Lyft_Type":Lyft_Type,"Uber_Type":Uber_Type,"cab_type_Uber":cab_type_Uber}

    return  jsonify({"price":str(result)})
# Press the green button in the gutter to run the script.
if __name__ == '__main__':
    app.run(debug=True)

