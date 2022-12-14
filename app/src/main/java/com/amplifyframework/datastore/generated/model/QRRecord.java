package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.AuthStrategy;
import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.ModelOperation;
import com.amplifyframework.core.model.annotations.AuthRule;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the QRRecord type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "QRRecords", type = Model.Type.USER, version = 1, authRules = {
  @AuthRule(allow = AuthStrategy.PUBLIC, operations = { ModelOperation.CREATE, ModelOperation.UPDATE, ModelOperation.DELETE, ModelOperation.READ })
})
public final class QRRecord implements Model {
  public static final QueryField ID = field("QRRecord", "id");
  public static final QueryField LAT = field("QRRecord", "lat");
  public static final QueryField LON = field("QRRecord", "lon");
  public static final QueryField QR_VALUE = field("QRRecord", "QRValue");
  public static final QueryField TIMESTAMP = field("QRRecord", "timestamp");
  public static final QueryField PURPOSE = field("QRRecord", "purpose");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="Float", isRequired = true) Double lat;
  private final @ModelField(targetType="Float", isRequired = true) Double lon;
  private final @ModelField(targetType="String", isRequired = true) String QRValue;
  private final @ModelField(targetType="AWSDateTime", isRequired = true) Temporal.DateTime timestamp;
  private final @ModelField(targetType="String") String purpose;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String resolveIdentifier() {
    return id;
  }
  
  public String getId() {
      return id;
  }
  
  public Double getLat() {
      return lat;
  }
  
  public Double getLon() {
      return lon;
  }
  
  public String getQrValue() {
      return QRValue;
  }
  
  public Temporal.DateTime getTimestamp() {
      return timestamp;
  }
  
  public String getPurpose() {
      return purpose;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private QRRecord(String id, Double lat, Double lon, String QRValue, Temporal.DateTime timestamp, String purpose) {
    this.id = id;
    this.lat = lat;
    this.lon = lon;
    this.QRValue = QRValue;
    this.timestamp = timestamp;
    this.purpose = purpose;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      QRRecord qrRecord = (QRRecord) obj;
      return ObjectsCompat.equals(getId(), qrRecord.getId()) &&
              ObjectsCompat.equals(getLat(), qrRecord.getLat()) &&
              ObjectsCompat.equals(getLon(), qrRecord.getLon()) &&
              ObjectsCompat.equals(getQrValue(), qrRecord.getQrValue()) &&
              ObjectsCompat.equals(getTimestamp(), qrRecord.getTimestamp()) &&
              ObjectsCompat.equals(getPurpose(), qrRecord.getPurpose()) &&
              ObjectsCompat.equals(getCreatedAt(), qrRecord.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), qrRecord.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getLat())
      .append(getLon())
      .append(getQrValue())
      .append(getTimestamp())
      .append(getPurpose())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("QRRecord {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("lat=" + String.valueOf(getLat()) + ", ")
      .append("lon=" + String.valueOf(getLon()) + ", ")
      .append("QRValue=" + String.valueOf(getQrValue()) + ", ")
      .append("timestamp=" + String.valueOf(getTimestamp()) + ", ")
      .append("purpose=" + String.valueOf(getPurpose()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static LatStep builder() {
      return new Builder();
  }
  
  /**
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static QRRecord justId(String id) {
    return new QRRecord(
      id,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      lat,
      lon,
      QRValue,
      timestamp,
      purpose);
  }
  public interface LatStep {
    LonStep lat(Double lat);
  }
  

  public interface LonStep {
    QrValueStep lon(Double lon);
  }
  

  public interface QrValueStep {
    TimestampStep qrValue(String qrValue);
  }
  

  public interface TimestampStep {
    BuildStep timestamp(Temporal.DateTime timestamp);
  }
  

  public interface BuildStep {
    QRRecord build();
    BuildStep id(String id);
    BuildStep purpose(String purpose);
  }
  

  public static class Builder implements LatStep, LonStep, QrValueStep, TimestampStep, BuildStep {
    private String id;
    private Double lat;
    private Double lon;
    private String QRValue;
    private Temporal.DateTime timestamp;
    private String purpose;
    @Override
     public QRRecord build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new QRRecord(
          id,
          lat,
          lon,
          QRValue,
          timestamp,
          purpose);
    }
    
    @Override
     public LonStep lat(Double lat) {
        Objects.requireNonNull(lat);
        this.lat = lat;
        return this;
    }
    
    @Override
     public QrValueStep lon(Double lon) {
        Objects.requireNonNull(lon);
        this.lon = lon;
        return this;
    }
    
    @Override
     public TimestampStep qrValue(String qrValue) {
        Objects.requireNonNull(qrValue);
        this.QRValue = qrValue;
        return this;
    }
    
    @Override
     public BuildStep timestamp(Temporal.DateTime timestamp) {
        Objects.requireNonNull(timestamp);
        this.timestamp = timestamp;
        return this;
    }
    
    @Override
     public BuildStep purpose(String purpose) {
        this.purpose = purpose;
        return this;
    }
    
    /**
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, Double lat, Double lon, String qrValue, Temporal.DateTime timestamp, String purpose) {
      super.id(id);
      super.lat(lat)
        .lon(lon)
        .qrValue(qrValue)
        .timestamp(timestamp)
        .purpose(purpose);
    }
    
    @Override
     public CopyOfBuilder lat(Double lat) {
      return (CopyOfBuilder) super.lat(lat);
    }
    
    @Override
     public CopyOfBuilder lon(Double lon) {
      return (CopyOfBuilder) super.lon(lon);
    }
    
    @Override
     public CopyOfBuilder qrValue(String qrValue) {
      return (CopyOfBuilder) super.qrValue(qrValue);
    }
    
    @Override
     public CopyOfBuilder timestamp(Temporal.DateTime timestamp) {
      return (CopyOfBuilder) super.timestamp(timestamp);
    }
    
    @Override
     public CopyOfBuilder purpose(String purpose) {
      return (CopyOfBuilder) super.purpose(purpose);
    }
  }
  
}
