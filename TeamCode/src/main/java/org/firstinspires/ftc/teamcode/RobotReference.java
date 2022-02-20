/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class RobotReference
{



    public Gamepad activeGamepad1 = null;
    public Gamepad activeGamepad2 = null;

    DigitalChannel armButton;

    double mmperin = 0.039701;

    int switchingConts = 0;

    double ticksperrotation = 537.6;
    double ticksperdegree = 3.95861111111;
    double wheeldiameter = 100;
    double pi = 3.1415;

    // 2.87

    static int LIFT_6 = 6200; // 1850;
    static int LIFT_5 = 5400; // 1850;
    static int LIFT_4 = 3444; // 1200;
    static int LIFT_3 = 1578; // 550;
    static int LIFT_2 = 947;  // 330;
    static int LIFT_1 = 344;  // 120;
    static int LIFT_0 = 00;

    static final double LIFT_ARM_ROTATE_PWR = 1;

    static int errorValue = 50;
    static int errorValueSlow = 10;






    /* Public OpMode members. */
    boolean choosingAuto = false;
    int autoMode = 0;


    int onSetting = 1;


    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();
    DcMotor leftFrontDrive = null;
    DcMotor rightFrontDrive = null;
    DcMotor leftBackDrive = null;
    DcMotor rightBackDrive = null;
    DcMotor robotArm = null;
    DcMotor intake = null;
    DcMotor spinnerR = null;
    DcMotor spinnerL = null;





    int LEVEL_5 = 5300; // 1850;
    int LEVEL_4 = 3444; // 1200;
    int LEVEL_3 = 1578; // 550;
    int LEVEL_2 = 947;  // 330;
    int LEVEL_1 = 344;  // 120;
    int LEVEL_0 = 0;


    boolean settingButtonDown1 = false;
    boolean settingButtonDown2 = false;


    double SPINNER_SPEED = .2;


    double FAST = 1;
    double MEDIUM = 0.6;
    double SLOW = 0.3;

    long SPIN_DURATION = 6750;

    

    boolean redOrBlue = true;

    int hubNum = 3;

    static final String VUFORIA_KEY =
            "AWm4j/n/////AAABmTASu/sajk1hgar/ycfLY5JgGop7sElDWeK3soXHJ2uHPc9oVGuCK0sX3RD2E1AhfDOHXj4kZv807ssyyK4L05Jgs+O6yQCXdx2COaW1P/lA3mGZg/sVOAN63z/udwYQ/lxQ/eDymyyuDhfHUk+zctnGk0ZAimwR8MAZ1KKHJ4GuD6zfdDnlvdcP/iXV+/ZnrtHDbZvn+PC9E8GUjsKIOKSeFxakH+fG9fbjOZGyUYy6RusVye1oo0exFKAV8CKDVP/ruVSXDYZfafmPBQunn4TUmnZQGFz5oxSiIcInXyoUgBmimLaKwdM+4wvxacTDz/svJ+4cVv8fh70uRDxHIpXD/HKoeLCyhkpNeoCMxxZk";


    VuforiaLocalizer vuforia;

    String[] settings = {
            "",
            "Duck Freight",
            "Duck No Freight",
            "Warehouse Freight",
            "Warehouse No Freight",
            ""};

    String TFOD_MODEL_ASSET = "FreightFrenzy_DM.tflite";
    String[] LABELS = {
            //"Ball",
            //"Cube",
            "Duck",
            "Marker"
    };
    TFObjectDetector tfod;


    /* local OpMode members. */
    HardwareMap hardwareMap           =  null;
    ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public RobotReference(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hardwareMap = ahwMap;

        // Define and Initialize Motors
        leftFrontDrive = hardwareMap.get(DcMotor.class, "fl");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "fr");
        leftBackDrive = hardwareMap.get(DcMotor.class, "bl");
        rightBackDrive = hardwareMap.get(DcMotor.class, "br");
        robotArm = hardwareMap.get(DcMotor.class, "ra");
        intake = hardwareMap.get(DcMotor.class, "in");
        spinnerR = hardwareMap.get(DcMotor.class, "sr");
        spinnerL = hardwareMap.get(DcMotor.class, "sl");

        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        spinnerR.setDirection(DcMotor.Direction.REVERSE);
        spinnerL.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);
        robotArm.setDirection(DcMotor.Direction.REVERSE);

        rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robotArm.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.REVERSE);

        rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotArm.setTargetPosition(0);
        robotArm.setPower(1);
        robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }
 }

