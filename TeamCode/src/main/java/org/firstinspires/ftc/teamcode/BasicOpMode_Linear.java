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

//here ya go DAVID i did it i did the thing i forgor :skull: what i did xd but it sure was something :D

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DigitalChannel;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 * <p>
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Basic: Linear OpMode", group = "Linear Opmode")
//@Disabled
public class BasicOpMode_Linear extends LinearOpMode {

    private RobotReference rb = new RobotReference();




    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        rb.init(hardwareMap);
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).

        rb.leftFrontDrive = hardwareMap.get(DcMotor.class, "fl");
        rb.rightFrontDrive = hardwareMap.get(DcMotor.class, "fr");
        rb.leftBackDrive = hardwareMap.get(DcMotor.class, "bl");
        rb.rightBackDrive = hardwareMap.get(DcMotor.class, "br");
        rb.robotArm = hardwareMap.get(DcMotor.class, "ra");
        rb.intake = hardwareMap.get(DcMotor.class, "in");
        rb.spinnerR = hardwareMap.get(DcMotor.class, "sr");
        rb.spinnerL = hardwareMap.get(DcMotor.class, "sl");

        rb.rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rb.leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rb.rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rb.leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rb.spinnerR.setDirection(DcMotor.Direction.FORWARD);
        rb.spinnerL.setDirection(DcMotor.Direction.FORWARD);
        rb.intake.setDirection(DcMotor.Direction.FORWARD);
        rb.robotArm.setDirection(DcMotor.Direction.REVERSE);

        //rb.armButton.setMode(DigitalChannel.Mode.INPUT);

        boolean facingFront = true;
        int toggled = 0;

        rb.robotArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rb.robotArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        int whereArm = 0;
        int ArmMoveTo = 0;

        boolean readyToCap = false;

        boolean cutscene = false;



        rb.activeGamepad1 = gamepad1;
        rb.activeGamepad2 = gamepad2;

        rb.robotArm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        rb.runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Setup a variable for each drive wheel to save power level for telemetry
            double moveX = rb.activeGamepad1.left_stick_x;
            double moveY = rb.activeGamepad1.left_stick_y;

            boolean spinLeft = gamepad2.left_bumper;
            boolean spinRight = gamepad2.right_bumper;

            boolean toggleButton = rb.activeGamepad1.x;

            boolean lift0 = gamepad2.dpad_down;
            boolean lift1 = gamepad2.dpad_left;
            boolean lift2 = gamepad2.dpad_right;
            boolean lift3 = gamepad2.dpad_up;
            boolean lift4 = gamepad2.back;
            boolean lift5 = gamepad2.b;

            double rotate = rb.activeGamepad1.right_stick_x;

            boolean fast = rb.activeGamepad1.right_stick_button;
            boolean altintake = rb.activeGamepad1.left_stick_button;

            boolean takingIn = rb.activeGamepad1.a || rb.activeGamepad2.y;
            boolean takingOut = rb.activeGamepad1.b;

            double armUp = rb.activeGamepad1.right_trigger;
            double armDown = rb.activeGamepad1.left_trigger;

            //boolean armIsDown = rb.armButton.getState();

            boolean armMoving;


                if (rb.activeGamepad1.back) {
                    cutscene = true;
                }

                if (facingFront) {
                    if (fast) {
                        rb.leftFrontDrive.setPower(moveY + rotate + moveX);
                        rb.rightFrontDrive.setPower(moveY - rotate - moveX);
                        rb.rightBackDrive.setPower(moveY - rotate + moveX);
                        rb.leftBackDrive.setPower(moveY + rotate - moveX);
                    } else {
                        rb.leftFrontDrive.setPower(((moveY / 2) + (rotate / 1.5) + (moveX / 2)));
                        rb.rightFrontDrive.setPower(((moveY / 2) - (rotate / 1.5) - (moveX / 2)));
                        rb.rightBackDrive.setPower(((moveY / 2) - (rotate / 1.5) + (moveX / 2)));
                        rb.leftBackDrive.setPower(((moveY / 2) + (rotate / 1.5) - (moveX / 2)));
                    }
                } else {
                    if (fast) {
                        rb.leftFrontDrive.setPower(-moveY + rotate - moveX);
                        rb.rightFrontDrive.setPower(-moveY - rotate + moveX);
                        rb.rightBackDrive.setPower(-moveY - rotate - moveX);
                        rb.leftBackDrive.setPower(-moveY + rotate + moveX);
                    } else {
                        rb.leftFrontDrive.setPower((-(moveY / 2) + (rotate / 1.5) - (moveX / 2)));
                        rb.rightFrontDrive.setPower((-(moveY / 2) - (rotate / 1.5) + (moveX / 2)));
                        rb.rightBackDrive.setPower((-(moveY / 2) - (rotate / 1.5) - (moveX / 2)));
                        rb.leftBackDrive.setPower((-(moveY / 2) + (rotate / 1.5    ) + (moveX / 2)));
                    }
                }

                if (lift0) {
                    rb.robotArm.setTargetPosition(rb.LIFT_0);
                    rb.robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rb.robotArm.setPower(rb.LIFT_ARM_ROTATE_PWR);
                }

                if (lift1) {
                    rb.robotArm.setTargetPosition(rb.LIFT_1);
                    rb.robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rb.robotArm.setPower(rb.LIFT_ARM_ROTATE_PWR);
                }

                if (lift2) {
                    rb.robotArm.setTargetPosition(rb.LIFT_2);
                    rb.robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rb.robotArm.setPower(rb.LIFT_ARM_ROTATE_PWR);
                }

                if (lift3) {
                    rb.robotArm.setTargetPosition(rb.LIFT_3);
                    rb.robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rb.robotArm.setPower(rb.LIFT_ARM_ROTATE_PWR);
                }

                if (lift4) {
                    rb.robotArm.setTargetPosition(rb.LIFT_4);
                    rb.robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rb.robotArm.setPower(rb.LIFT_ARM_ROTATE_PWR);

                }

                if (lift5) {
                    rb.robotArm.setTargetPosition(rb.LIFT_5);
                    rb.robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rb.robotArm.setPower(rb.LIFT_ARM_ROTATE_PWR);

                }

                if (gamepad2.a && rb.robotArm.getCurrentPosition() > 3157) { // 1100) {
                    readyToCap = false;
                    rb.robotArm.setTargetPosition(rb.LIFT_5);
                    rb.robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    rb.robotArm.setPower(0.5);
                }

                if (rb.activeGamepad1.start) {
                    rb.rightBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rb.leftBackDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rb.rightFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    rb.leftFrontDrive.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                }

                if (toggleButton == true && toggled == 0) {
                    toggled = 1;
                    facingFront = false;
                }

                if (toggleButton == false && toggled == 1) {
                    toggled = 2;
                }

                if (toggleButton == true && toggled == 2) {
                    toggled = 3;
                    facingFront = true;
                }

                if (toggleButton == false && toggled == 3) {
                    toggled = 0;
                }

                if (takingIn || altintake) {
                    rb.intake.setPower(1);
                }

                if (takingOut) {
                    rb.intake.setPower(-1);
                }

                if ((takingOut == false) && (takingIn == false)) {
                    rb.intake.setPower(0);
                }

                /*
                cap:1940
                pickup:50
                layer 3:2130
                layer 2: 590
                layer 1: 330
                */

                // telemetry.addData("FRONT LEFT", leftFrontDrive.getCurrentPosition());
                // telemetry.addData("FRONT RIGHT", rightFrontDrive.getCurrentPosition());
                // telemetry.addData("BACK LEFT", leftBackDrive.getCurrentPosition());
                // telemetry.addData("BACK RIGHT", rightBackDrive.getCurrentPosition());
                telemetry.addData("armPosition", rb.robotArm.getCurrentPosition());

                if (spinLeft) {
                    rb.spinnerR.setPower(0.5);
                    rb.spinnerL.setPower(0.5);
                } else {
                    if (spinRight) {
                        rb.spinnerR.setPower(-0.5);
                        rb.spinnerL.setPower(-0.5);
                    } else {
                        rb.spinnerR.setPower(0);
                        rb.spinnerL.setPower(0);
                    }
                }
                /* on comment indefinetly
                if (armUp > 0.3) {
                    robotArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                    robotArm.setPower(0.5);
                    whereArm = robotArm.getCurrentPosition();
                } else {
                    if (armDown > 0.3 && robotArm.getCurrentPosition() > 50) {
                        robotArm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                        robotArm.setPower(-0.5);
                        whereArm = robotArm.getCurrentPosition();
                    } else {
                        robotArm.setPower(1);
                        robotArm.setTargetPosition(whereArm);
                        robotArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    }
                }*/




            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + rb.runtime.toString());
            // telemetry.addData("", toggled);
            // telemetry.addData("Spinner", "left (%.2f)", activeGamepad1.right_trigger);
            telemetry.update();
        }
    }





}