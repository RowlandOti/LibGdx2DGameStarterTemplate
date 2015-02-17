
package com.magnetideas.smoothcam;

import java.util.ArrayList;
import java.util.Arrays;

/** Provides smooth camera movement between one {@link SmoothCamSubject} and a set of {@link SmoothCamPoint}
 * @author David Froehlich <semperhilaris@gmail.com> */
public class SmoothCamWorld {
	private SmoothCamSubject subject;
	private SmoothCamBoundingBox boundingBox = new SmoothCamBoundingBox(0, 0);
	private SmoothCamPoint[] points = {};
	private float x = 0f;
	private float y = 0f;
	private float zoom = 1f;
	private boolean useBoundingBox = false;
	private float fixedXvalue = 0f;
	private float fixedYvalue = 0f;
	public boolean fixedX = false;
	public boolean fixedY = false;

	public SmoothCamWorld()
	{

	}
	/** Creates a World for SmoothCam and sets its subject
	 * @param subject */
	public SmoothCamWorld (SmoothCamSubject subject) {
		this.subject = subject;
	}

	public void setSubject(SmoothCamSubject subject) {
		this.subject = subject;
	}
	/** Adds a {@link SmoothCamPoint} to the world
	 * @param point */
	public void addPoint (SmoothCamPoint point) {
		SmoothCamPoint[] newPoints = new SmoothCamPoint[points.length + 1];
		newPoints[0] = point;
		System.arraycopy(points, 0, newPoints, 1, points.length);
		points = newPoints;
	}

	/** Removes a {@link SmoothCamPoint} from the world
	 * @param point */
	public void removePoint (SmoothCamPoint point) {
		ArrayList<SmoothCamPoint> temp = new ArrayList<SmoothCamPoint>(Arrays.asList(points));
		temp.remove(point);
		points = temp.toArray(new SmoothCamPoint[temp.size()]);
	}

	public SmoothCamPoint[] getPoints () {
		return points;
	}

	public float getX () {
		return x;
	}

	public float getY () {
		return y;
	}

	public float getZoom () {
		return zoom;
	}

	public SmoothCamSubject getSubject () {
		return subject;
	}

	public double getDistance (SmoothCamPoint p1, SmoothCamPoint p2) {
		return Math.sqrt((p1.getX() - p2.getX()) * (p1.getX() - p2.getX()) + (p1.getY() - p2.getY()) * (p1.getY() - p2.getY()));
	}

	public void setBoundingBox (float w, float h) {
		boundingBox.setBounds(w, h);
		if (w > 0 && h > 0) {
			useBoundingBox = true;
		} else {
			useBoundingBox = false;
		}
	}

	public void setX (float x) {
		this.x = x;
	}

	public void setY (float y) {
		this.y = y;
	}

	public void setZoom (float zoom) {
		this.zoom = zoom;
	}

	public void setFixedX (float fixedValue) {
		fixedX = true;
		fixedXvalue = fixedValue;
	}

	public void setFixedY (float fixedValue) {
		fixedY = true;
		fixedYvalue = fixedValue;
	}

	public void setFixedAxis (float fixedX, float fixedY) {
		setFixedX(fixedX);
		setFixedY(fixedY);
	}

	public void freeFixedX () {
		fixedX = false;
	}

	public void freeFixedY () {
		fixedY = false;
	}

	public void freeFixedAxis () {
		freeFixedX();
		freeFixedY();
	}

	public SmoothCamBoundingBox getBoundingBox () {
		return boundingBox;
	}

	/** Calculates the nearest point of interest relative to the world's subject. If the subject is already inside the outer radius
	 * of a point, that point will be returned instead.
	 * @param p1
	 * @return {@link SmoothCamPoint} */
	public SmoothCamPoint getNearestPoint (SmoothCamPoint p1) {
		double distance = 0;
		double currentBest = 0;
		SmoothCamPoint nearestPoint = new SmoothCamPoint();
		for (int i = 0; i < points.length; i++) {
			distance = getDistance(p1, points[i]);
			if (distance <= points[i].getOuterRadius()) {
				return points[i];
			} else if (distance < currentBest || currentBest == 0) {
				nearestPoint = points[i];
				currentBest = distance;
			}
		}
		return nearestPoint;
	}

	/** Calculates the focus point of the camera. If the subject is not inside the outer radius of a point of interest, the camera
	 * will follow the subject and only apply velocity shifting. When the subject enters the outer radius of a point of interest,
	 * the focus will begin to shift from the subject towards that point. Once the subject entered the inner radius of the point,
	 * camera focus switches completely to the point of interest. */
	public void update () {
		float coeff = 0f;
		if (points.length > 0) {
			SmoothCamPoint nearestPoint = getNearestPoint(this.subject);
			double distance = getDistance(subject, nearestPoint);
			if (distance > nearestPoint.getOuterRadius()) {
				if (!fixedX) x = subject.getX();
				if (!fixedY) y = subject.getY();
				coeff = 1f;
			} else {
				coeff = ((float)distance - nearestPoint.getInnerRadius()) / nearestPoint.getRadiusDistance();
				if (coeff < 0) {
					coeff = 0f;
				}
				float deltaX = subject.getX() - nearestPoint.getX();
				float deltaY = subject.getY() - nearestPoint.getY();
				if (nearestPoint.getPolarity() == SmoothCamPoint.REPULSE) {
					if (distance == 0) distance = 0.0001; // avoiding division by zero
					if (!fixedX) x = subject.getX() + deltaX / (float)distance * (nearestPoint.getOuterRadius() - (float)distance);
					if (!fixedY) y = subject.getY() + deltaY / (float)distance * (nearestPoint.getOuterRadius() - (float)distance);
				} else {
					if (!fixedX) x = nearestPoint.getX() + coeff * deltaX;
					if (!fixedY) y = nearestPoint.getY() + coeff * deltaY;
				}
				zoom = 1f + (nearestPoint.getZoom() - nearestPoint.getZoom() * coeff);
			}
		} else {
			if (!fixedX) x = subject.getX();
			if (!fixedY) y = subject.getY();
			coeff = 1f;
		}

		if (subject.getVelocityRadius() > 0) {
			x = x + (subject.getVelocityRadius() * subject.getVelocityX() * coeff);
			y = y + (subject.getVelocityRadius() * subject.getVelocityY() * coeff);
		}

		if (subject.getAimingRadius() > 0) {
			x = x + (subject.getAimingRadius() * subject.getAimingX() * coeff);
			y = y + (subject.getAimingRadius() * subject.getAimingY() * coeff);
		}

		if (fixedX) {
			x = fixedXvalue;
		}
		if (fixedY) {
			y = fixedYvalue;
		}

		if (useBoundingBox) {
			if (subject.getX() > x + boundingBox.w2) {
				x = subject.getX() - boundingBox.w2;
			}
			if (subject.getX() < x - boundingBox.w2) {
				x = subject.getX() + boundingBox.w2;
			}
			if (subject.getY() > y + boundingBox.h2) {
				y = subject.getY() - boundingBox.h2;
			}
			if (subject.getY() < y - boundingBox.h2) {
				y = subject.getY() + boundingBox.h2;
			}
		}
	}

}
