package nope.notsdsmt.dgfae.pongbrubakernoah;

public abstract class CollisionElement {
    public PongView.Shape shape;

    public CollisionElement(PongView.Shape shape){
        this.shape = shape;
    }

    public boolean colliding( CollisionElement target ){
        double dx = shape.posX - target.shape.posX;
        double dy = shape.posY - target.shape.posY;
        if( shape instanceof PongView.Circle ){
            if( target.shape instanceof PongView.Circle ){
                double radius = ((PongView.Circle)target.shape).radius + ((PongView.Circle)shape).radius;
                if( dx * dx + dy * dy < radius * radius){
                    onCollide(target);
                    return true;
                }
            } else {
                double dxr = dx * Math.cos(target.shape.theta) - dy * Math.sin(target.shape.theta);
                double dyr = dx * Math.sin(target.shape.theta) + dy * Math.cos(target.shape.theta);
                if( Math.abs(dxr) - ((PongView.Circle) shape).radius < target.shape.wid / 2
                        && Math.abs(dyr) < target.shape.hit / 2
                        || Math.abs(dxr) < target.shape.wid / 2
                        && Math.abs(dyr) - ((PongView.Circle) shape).radius < target.shape.hit / 2){
                    onCollide(target);
                    return true;
                }
            }
        } else {
            if( target.shape instanceof PongView.Circle ){
                double dxr = dx * Math.cos(shape.theta) - dy * Math.sin(shape.theta);
                double dyr = dx * Math.sin(shape.theta) + dy * Math.cos(shape.theta);
                if( Math.abs(dxr) - ((PongView.Circle) target.shape).radius < shape.wid / 2
                        && Math.abs(dyr) < shape.hit / 2
                        || Math.abs(dxr) < shape.wid / 2
                        && Math.abs(dyr) - ((PongView.Circle) target.shape).radius < shape.hit / 2){
                    onCollide(target);
                    return true;
                }
            } else {
                System.out.println("This really should never happen.");
                return false;
            }
        }
        return false;
    }

    public abstract void onCollide( CollisionElement target );
}