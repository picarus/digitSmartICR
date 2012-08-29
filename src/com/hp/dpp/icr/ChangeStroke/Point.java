package com.hp.dpp.icr.ChangeStroke;

import com.hp.dpp.sdk.SampleIterator;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: JOMAG
 * Date: Oct 2, 2003
 * Time: 9:42:28 PM
 * To change this template use Options | File Templates.
 */
 public class Point implements Serializable {
    float x_;
    float y_;

     public Point() {
        x_ = 0f;
        y_ = 0f;
    }

     public Point(float x, float y) {
        x_ = x;
        y_ = y;
    }

     public Point(SampleIterator si) {
        x_ = si.getX();
        y_ = si.getY();
    }

     public Point subs(Point p) {
        return new Point(x_ - p.x_, y_ - p.y_);
    }

     public Point add(Point p) {
        return new Point(x_ + p.x_, y_ + p.y_);
    }


     public Point substract(Point p) {
        x_ = x_ - p.x_;
        y_ = y_ - p.y_;
        return this;
    }

     public Point addition(Point p) {
        x_ = x_ + p.x_;
        y_ = y_ + p.y_;
        return this;
    }

     public Point multiply(Point p) {
        x_ = x_ * p.x_;
        y_ = y_ * p.y_;
        return this;
    }

     public Point scale(float factor) {
        return new Point(x_ / factor, y_ / factor);
    }

     public void toInt() {
        x_ = (int) x_;
        y_ = (int) y_;
    }

     public Point divide(Point p) {
        x_ = x_ / p.x_;
        y_ = y_ / p.y_;
        return this;
    }

     public Point copy() {
        return new Point(x_, y_);
    }

     public Point changes(Point diff) {
        return new Point(x_ * diff.x_, y_ * diff.y_);
    }

     public void update(Point p) {
        x_ = p.x_;
        y_ = p.y_;
    }

     public void setX(float x) {
        x_ = x;
    }

     public void setY(float y) {
        y_ = y;
    }

    public float getX() {
       return x_;
   }

    public float getY() {
       return y_;
   }

}
