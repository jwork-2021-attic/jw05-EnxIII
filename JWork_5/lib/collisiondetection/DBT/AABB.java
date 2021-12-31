package JWork_5.lib.collisiondetection.DBT;

import java.lang.Math;

public class AABB {

    private float x1, y1, x2, y2;

    public AABB(){
        this.x1 = this.x2 = 0f;
        this.y1 = this.y2 = 0f;
    }

    public AABB(float x1, float y1, float x2, float y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public double surfaceArea(){
        return (getWidth() * getHeight());
    }

    public boolean overlaps(AABB other){
        return x2 > other.x1 &&
                x1 < other.x2 &&
                y2 > other.y1 &&
                y1 < other.y2;
    }

    public boolean contains(AABB other){
        return other.x1 >= x1 &&
                other.x2 <= x2 &&
                other.y1 >= y1 &&
                other.y2 <= y2;
    }

    public void updateAABB(AABB aabb){
        x1 = Math.min(aabb.x1, x1);
        y1 = Math.min(aabb.y1, y1);
        x2 = Math.max(aabb.x2, x2);
        y2 = Math.max(aabb.y2, y2);
    }

    public void merge(AABB other1, AABB other2) {
        this.x1 = Math.min(other1.x1, other2.x1);
        this.y1 = Math.min(other1.y1, other2.y1);
        this.x2 = Math.max(other1.x2, other2.x2);
        this.y2 = Math.max(other1.y2, other2.y2);
    }

    public void intersection(AABB other, AABB result) {
        result.x1 = Math.max(this.x1, other.x1);
        result.y1 = Math.max(this.y1, other.y1);
        result.x2 = Math.min(this.x2, other.x2);
        result.y2 = Math.min(this.y2, other.y2);
    }

    public void clear(){
        x1 = 0;
        x2 = 0;
        y1 = 0;
        y2 = 0;
    }

    public void copy(AABB other){
        this.x1 = other.x1;
        this.y1 = other.y1;
        this.x2 = other.x2;
        this.y2 = other.y2;
    }

    public void setValue(float x1, float y1, float x2, float y2){
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void extend(float margin){
        this.x1 -= margin;
        this.y1 -= margin;
        this.x2 += margin;
        this.y2 += margin;
    }

    public float getStartX(){ return this.x1; }
    public float getStartY(){ return this.y1; }
    public float getWidth() { return x2 - x1; }
    public float getHeight() { return y2 - y1; }


}

