import numpy
from math import sin, cos
import matplotlib.pyplot as plt
import iptrack
import trvalues

# Constants for the track
ROLLING_FRICTION_CONSTANT = 0.4
LENGTH_OF_OBSERVATIONS = 7
BALL_MASS = 0.0303 #mass in kg

# Starting position of the ball
START_POINT_T = 0.0
global START_POINT_X
START_POINT_X = 0
START_SPEED = 0.0

# General constants
g = 9.81
h = 0.0001


def read_from_file(filepath):
    observed_t_values = []
    observed_x_values = []
    observed_y_values = []

    with open(filepath) as file:
        file.readline()
        file.readline()
        for line in file:
            line = line.replace(",", ".")
            line = line.strip("\n")
            t = float(line.split("\t")[0])
            x = float(line.split("\t")[1]) / 100
            y = float(line.split("\t")[2]) / 100
            observed_t_values.append(t)
            observed_y_values.append(y)
            observed_x_values.append(x)
    global START_POINT_X
    START_POINT_X = observed_x_values[0]
    return observed_t_values, observed_x_values, observed_y_values


def acceleration(iteration_number):
    next_a = g * sin(d_values[iteration_number]) / (1 + ROLLING_FRICTION_CONSTANT)
    if PRINT_OUPUT:
        print("A: " + str(next_a))
    return next_a


def next_x(iteration_number):
    delta_x = v_values[iteration_number] * h * cos(d_values[iteration_number])
    next_x = x_values[iteration_number] + delta_x
    if PRINT_OUPUT:
        print("X: " + str(next_x))
    return next_x


def next_v(iteration_number):
    delta_v = a_values[iteration_number] * h
    next_v =  v_values[iteration_number - 1] + delta_v
    if PRINT_OUPUT:
        print("Velocity: " + str(next_v))
    return next_v

def next_f(iteration_number):
    next_f = abs(2/5 * BALL_MASS * a_values[iteration_number])
    return next_f

def next_normal(iteration_number, R):
    next_norm = abs(g) * sin(d_values[iteration_number]) * BALL_MASS + (BALL_MASS * v_values[iteration_number]**2) / R
    return next_norm


def do_numerical_calculations(timestep_to_break):
    for iteration in range(0, 100000000):
        if timestep_to_break < t_values[-1]:
            break
        try:
            x = next_x(iteration)
            [y, _, _, degrees, R] = trvalues.trvalues(polynomyal, x)
            t_values.append(t_values[-1] + h)
            x_values.append(x)
            y_values.append(y)

            d_values.append(degrees)
            a_values.append(acceleration(iteration + 1))
            v_values.append(next_v(iteration + 1))
            f_values.append(next_f(iteration + 1))
            normal_f_values.append(next_normal(iteration + 1, R))
            if PRINT_OUPUT:
                print("Degrees: " + str(numpy.degrees(degrees)))
                print(" ")
        except Exception as e:
            print(e)
            print("-------------------------------------------------")
            print("Crashed at iteration: " + str(iteration))
            break


def calculate_energy_loss_rate(x_values, y_values):
    left_to_right = True

    previous_value = x_values[0]
    oscillations_heights = [y_values[0]]
    for i in range(10, len(x_values)):
        next_value = x_values[i]
        if next_value < previous_value and left_to_right:
            oscillations_heights.append(y_values[i])
            left_to_right = False
        elif next_value > previous_value and not left_to_right:
            oscillations_heights.append(y_values[i])
            left_to_right = True
        previous_value = next_value

    energy_loss_rate_half = []
    x_energy_loss_rate_half = []
    energy_loss_rate_whole = []
    x_energy_loss_rate_whole = []
    for i in range(0, len(oscillations_heights) - 1):
        first = oscillations_heights[i]
        second = oscillations_heights[i + 1]
        height_loss_rate = second / first
        energy_loss_rate_half.append(height_loss_rate)
        x_energy_loss_rate_half.append(i)
    for i in range(0, len(oscillations_heights) - 2, 2):
        first = oscillations_heights[i]
        second = oscillations_heights[i + 2]
        height_loss_rate = second / first
        energy_loss_rate_whole.append(height_loss_rate)
        x_energy_loss_rate_whole.append(i / 2)
    return x_energy_loss_rate_half, energy_loss_rate_half, x_energy_loss_rate_whole, energy_loss_rate_whole


def plot_polynom(polynom):
    axes = plt.gca()
    axes.set_xlim([0, 7])
    x_list = []
    y_list = []
    x_start = -0.7
    for x in range(140):
        x = x_start + x / 100
        [y, _, _, _, _] = trvalues.trvalues(polynom, x)
        x_list.append(x)
        y_list.append(y)
    plt.plot(x_list, y_list)


filepath = "forsøk1.txt"
data_t_values, data_x_values, data_y_values = read_from_file(filepath)
polynomyal = iptrack.iptrack("NewInterpolationPoints.txt")
PRINT_OUPUT = False
print(polynomyal)



# Numerical calculations
[y, _, _, start_degrees, R] = trvalues.trvalues(polynomyal, START_POINT_X)
t_values = [START_POINT_T]
x_values = [START_POINT_X]
y_values = [y]
d_values = [start_degrees]
a_values = [acceleration(0)]
v_values = [START_SPEED]
f_values = [next_f(0)] #rolling friction
normal_f_values = [next_normal(0, R)] #normal force



def poly(x):
    return 5.05669847*x**8 - 0.89041848*x**7 - 3.98585356*x**6 + 0.49460653*x**5 + 2.60947658*x**4 - 0.17042107*x**3 + 0.5219725*x**2 - 0.02032178*x + 0.01244064

x_list = []
y_list = []

x_start = -0.6
for h in range(30, 1200):
    x = x_start + h / 1000
    [y, _, _, start_degrees, R] = trvalues.trvalues(polynomyal, x)
    x_list.append(x)
    y_list.append(y)

plt.plot(x_list, y_list)
plt.show()


do_numerical_calculations(LENGTH_OF_OBSERVATIONS)

x_energy_loss_rate_half, energy_loss_rate_half, x_energy_loss_rate_whole, energy_loss_rate_whole = calculate_energy_loss_rate(x_values, y_values)
obs_x_energy_loss_rate_half, obs_energy_loss_rate_half, obs_x_energy_loss_rate_whole, obs_energy_loss_rate_whole = calculate_energy_loss_rate(data_x_values, data_y_values)

plt.plot(t_values, y_values)
plt.plot(data_t_values, data_y_values)
plt.show()
