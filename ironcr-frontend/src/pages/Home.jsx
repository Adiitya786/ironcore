import Hero from "../components/home/Hero";
import Stats from "../components/home/Stats";
import WhyChooseUs from "../components/home/WhyChooseUs";
import ProgramsPreview from "../components/home/ProgramsPreview";
import EquipmentPreview from "../components/home/EquipmentPreview";
import TrainersPreview from "../components/home/TrainersPreview";
import MembershipPreview from "../components/home/MembershipPreview";
import TrialCTA from "../components/home/TrialCTA";
import GalleryPreview from "../components/home/GalleryPreview";
import Testimonials from "../components/home/Testimonials";
import FAQPreview from "../components/home/FAQPreview";
import LocationCTA from "../components/home/LocationCTA";
import FinalCTA from "../components/home/FinalCTA";

export default function Home() {
  return (
    <>
      <Hero />
      <Stats />
      <WhyChooseUs />
      <ProgramsPreview />
      <EquipmentPreview />
      <TrainersPreview />
      <MembershipPreview />
      <TrialCTA />
      <GalleryPreview />
      <Testimonials />
      <FAQPreview />
      <LocationCTA />
      <FinalCTA />
    </>
  );
}
