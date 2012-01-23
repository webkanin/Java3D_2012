package chapter7;

import javax.vecmath.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;

public class quatToEuler  {

    public static Vector3d quat2Euler(Quat4d q1) {
        double sqw = q1.w*q1.w;
        double sqx = q1.x*q1.x;
        double sqy = q1.y*q1.y;
        double sqz = q1.z*q1.z;
        double heading = Math.atan2(2.0 * (q1.x*q1.y + q1.z*q1.w),(sqx - sqy - sqz + sqw));
        double bank = Math.atan2(2.0 * (q1.y*q1.z + q1.x*q1.w),(-sqx - sqy + sqz + sqw));
        double attitude = Math.asin(-2.0 * (q1.x*q1.z - q1.y*q1.w));
        return new Vector3d(bank, attitude, heading);
    }
}