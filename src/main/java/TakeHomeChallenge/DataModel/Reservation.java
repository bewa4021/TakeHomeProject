package TakeHomeChallenge.DataModel;

import java.util.Date;

public class Reservation {

  private int campsiteId;

  private Date endDate;

  private Date startDate;

  public Reservation(int id, Date startDate, Date endDate) {
    this.campsiteId = id;
    this.startDate = startDate;
    this.endDate = endDate;
  }

  public int getCampsiteId() {
    return campsiteId;
  }

  public Date getEndDate() {
    return endDate;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }
}
