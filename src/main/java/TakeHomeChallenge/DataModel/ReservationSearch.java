package TakeHomeChallenge.DataModel;

import java.util.Date;

public class ReservationSearch {

  private Date endDate;

  private Date startDate;

  public ReservationSearch(Date startDate, Date endDate) {
    this.endDate = endDate;
    this.startDate = startDate;
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
