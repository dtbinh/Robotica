from bottle import route, run, template

import vrep
import time
import sys

print ('Program started')
vrep.simxFinish(-1) # just in case, close all opened connections
clientID=vrep.simxStart('127.0.0.1',19997,True,True,5000,5)

wheelRadius = 0.04
linearVelocityLeft = 0.01
linearVelocityRight = 0.01

res,leftJointDynamic = vrep.simxGetObjectHandle(clientID, "Pioneer_p3dx_leftMotor", vrep.simx_opmode_oneshot_wait)
res,rightJointDynamic = vrep.simxGetObjectHandle(clientID, "Pioneer_p3dx_rightMotor", vrep.simx_opmode_oneshot_wait)

def set_motors():
    global linearVelocityLeft, linearVelocityRight, wheelRadius, vrep, clientID, leftJointDynamic, rightJointDynamic
    t1 = linearVelocityLeft/wheelRadius
    t2 = linearVelocityRight/wheelRadius
    print t1, t2
    vrep.simxSetJointTargetVelocity(clientID, leftJointDynamic, t1, vrep.simx_opmode_oneshot_wait)
    vrep.simxSetJointTargetVelocity(clientID, rightJointDynamic, t2, vrep.simx_opmode_oneshot_wait)

@route('/arriba')
def up():
    global linearVelocityLeft, linearVelocityRight, wheelRadius, vrep, clientID, leftJointDynamic, rightJointDynamic
    linearVelocityLeft += 0.01
    linearVelocityRight += 0.01
    set_motors()
    return template('control')

@route('/abajo')
def down():
    global linearVelocityLeft, linearVelocityRight, wheelRadius, vrep, clientID, leftJointDynamic, rightJointDynamic
    linearVelocityLeft -= 0.01
    linearVelocityRight -= 0.01
    set_motors()
    return template('control')

@route('/izquierda')
def left():
    global linearVelocityLeft, linearVelocityRight, wheelRadius, vrep, clientID, leftJointDynamic, rightJointDynamic
    linearVelocityLeft -= 0.01
    linearVelocityRight += 0.01
    set_motors()
    return template('control')

@route('/derecha')
def right():
    global linearVelocityLeft, linearVelocityRight, wheelRadius, vrep, clientID, leftJointDynamic, rightJointDynamic
    linearVelocityLeft += 0.01
    linearVelocityRight -= 0.01
    set_motors()
    return template('control')

run(host='localhost', port=8889)
