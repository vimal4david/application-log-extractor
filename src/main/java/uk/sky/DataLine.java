package uk.sky;

import java.util.Objects;

class DataLine {
  private final long requestTimestamp;
  private final String countryCode;
  private final long responseTime;

  public DataLine(long requestTimestamp, String countryCode, long responseTime) {
    this.requestTimestamp = requestTimestamp;
    this.countryCode = countryCode;
    this.responseTime = responseTime;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public long getResponseTime() {
    return responseTime;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataLine dataLine = (DataLine) o;
    return requestTimestamp == dataLine.requestTimestamp
        && responseTime == dataLine.responseTime
        && Objects.equals(countryCode, dataLine.countryCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(requestTimestamp, countryCode, responseTime);
  }
}
