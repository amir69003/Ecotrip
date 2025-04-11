import { useMap } from "react-leaflet";
import { useEffect } from "react";

type ChangeViewProps = {
  coords: [number, number][];
};

const ChangeView = ({ coords }: ChangeViewProps) => {
  const map = useMap();

  useEffect(() => {
    if (coords.length > 0) {
      const bounds = coords.map(([lat, lng]) => [lat, lng]) as [number, number][];
      map.fitBounds(bounds);
    }
  }, [coords, map]);

  return null;
};

export default ChangeView;
